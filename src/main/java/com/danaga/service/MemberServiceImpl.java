package com.danaga.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danaga.dao.MemberDao;
import com.danaga.dto.KakaoMemberUpdateDto;
import com.danaga.dao.OrderDao;
import com.danaga.dto.MemberInsertGuestDto;
import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.MemberUpdateDto;
import com.danaga.entity.Member;
import com.danaga.exception.PasswordMismatchException;
import com.danaga.exception.EmailMismatchException;
import com.danaga.exception.ExistedMemberByEmailException;
import com.danaga.exception.ExistedMemberByNicknameException;
import com.danaga.exception.ExistedMemberByUserNameException;
import com.danaga.exception.ExistedMemberByPhoneNoException;
import com.danaga.exception.MemberNotFoundException;
import com.danaga.repository.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OrderDao orderDao;

	public List<MemberResponseDto> getMembers() {
		List<Member> members = memberDao.findMembers();
		List<MemberResponseDto> responseMembers = new ArrayList<>();
		for (Member member : members) {
			responseMembers.add(MemberResponseDto.toDto(member));
		}
		return responseMembers;
	}

	public MemberResponseDto getMemberBy(String value) throws Exception {
		return MemberResponseDto.toDto(memberDao.findMember(value));
	}

	@Transactional
	public MemberResponseDto joinMember(Member member) throws Exception, ExistedMemberByUserNameException {
		// 1.중복체크
		if (memberDao.existedMemberByUserName(member.getUserName())) {
			// 중복
			throw new ExistedMemberByUserNameException(member.getUserName() + " 는 이미 존재하는 아이디 입니다.");
		} else if (memberDao.existedMemberByEmail(member.getEmail())) {
			throw new ExistedMemberByEmailException(member.getEmail() + " 는 이미 등록된 이메일 입니다.");
		} else if (memberDao.existedMemberByPhoneNo(member.getPhoneNo())
				&& (memberDao.findMember(member.getPhoneNo()).getRole().equals("Guest"))) {
			member.setRole("Member");
			return MemberResponseDto.toDto((memberDao.update(member)));
		} else if (memberDao.existedMemberByPhoneNo(member.getPhoneNo())) {
			throw new ExistedMemberByPhoneNoException(member.getPhoneNo() + " 는 이미 등록된 번호 입니다.");
		} else if (memberRepository.findByNickname(member.getNickname()).isPresent()) {
			throw new ExistedMemberByNicknameException(member.getNickname() + "는 사용중인 닉네임 입니다.");
		}
		// 안중복
		// 2.회원가입
		return MemberResponseDto.toDto(memberDao.insert(member));
	}

	@Transactional
	public MemberResponseDto joinGuest(MemberInsertGuestDto memberInsertGuestDto) throws Exception {
		if (memberDao.existedMemberByEmail(memberInsertGuestDto.getEmail())
				&& memberDao.existedMemberByPhoneNo(memberInsertGuestDto.getPhoneNo())) {
			Member member = memberDao.findMember((memberInsertGuestDto).getPhoneNo());
			member.setEmail(null);
			memberDao.updateGuestEmail(member);
			return MemberResponseDto.toDto(member);
		} else if (memberDao.existedMemberByEmail(memberInsertGuestDto.getEmail())) {
			Member member = memberDao.findMember((memberInsertGuestDto).getEmail());
			member.setPhoneNo(memberInsertGuestDto.getPhoneNo());
			memberDao.updateGuestPhoneNo(member);
			return MemberResponseDto.toDto(member);
		} else if (memberDao.existedMemberByPhoneNo(memberInsertGuestDto.getPhoneNo())) {
			Member member = memberDao.findMember((memberInsertGuestDto).getPhoneNo());
			member.setEmail(memberInsertGuestDto.getEmail());
			memberDao.updateGuestEmail(member);
			return MemberResponseDto.toDto(member);
		} else {
			return MemberResponseDto.toDto(memberDao.insert(Member.toGuestEntity(memberInsertGuestDto)));
		}
	}

	@Transactional
	public MemberResponseDto updateMember(MemberUpdateDto memberUpdateDto)
			throws Exception, ExistedMemberByNicknameException {
		Member originalMember = memberRepository.findById(memberUpdateDto.getId()).get();
		Member member = Member.builder().id(memberUpdateDto.getId()).userName(originalMember.getUserName())
				.password(memberUpdateDto.getPassword()).nickname(memberUpdateDto.getNickname())
				.postCode(memberUpdateDto.getPostCode()).address(memberUpdateDto.getAddress())
				.detailAddress(memberUpdateDto.getDetailAddress()).role(originalMember.getRole())
				.grade(originalMember.getGrade()).gradePoint(originalMember.getGradePoint()).build();

		if (originalMember.getNickname().equals(member.getNickname())) {

		} else if (memberRepository.findByNickname(member.getNickname()).isPresent()) {
			throw new ExistedMemberByNicknameException(member.getNickname() + "는 사용중인 닉네임 입니다.");
		}
		// Member updatedMember = Member.toUpdateEntity(memberUpdateDto);
		return MemberResponseDto.toDto(memberDao.update(member));
	}

	@Transactional
	public MemberResponseDto updateKakaoMember(KakaoMemberUpdateDto kakaoMemberUpdateDto)
			throws Exception, ExistedMemberByNicknameException {
		Member originalMember = memberRepository.findById(kakaoMemberUpdateDto.getId()).get();
		Member member = Member.builder().id(kakaoMemberUpdateDto.getId()).userName(kakaoMemberUpdateDto.getUserName())
				.password(kakaoMemberUpdateDto.getPassword()).nickname(kakaoMemberUpdateDto.getNickname())
				.postCode(kakaoMemberUpdateDto.getPostCode()).address(kakaoMemberUpdateDto.getAddress())
				.detailAddress(kakaoMemberUpdateDto.getDetailAddress()).role("Member").grade(originalMember.getGrade())
				.gradePoint(originalMember.getGradePoint()).build();
		if (originalMember.getNickname().equals(member.getNickname())) {

		} else if (memberRepository.findByNickname(member.getNickname()).isPresent()) {
			throw new ExistedMemberByNicknameException(member.getNickname() + "는 사용중인 닉네임 입니다.");
		}
		// Member updatedMember = Member.toUpdateEntity(memberUpdateDto);
		return MemberResponseDto.toDto(memberDao.update(member));
	}

	@Transactional
	public void deleteMember(String value) throws Exception {
		orderDao.ordersMemberIdNull(value);
		memberDao.delete(value);
	}

	// 중복체크
	public boolean isDuplicateByUserName(String userName) throws Exception {
		return memberDao.existedMemberByUserName(userName);
	}

	public boolean isDuplicateByEmail(String email) throws Exception {
		return memberDao.existedMemberByEmail(email);
	}

	public boolean isDuplicateByPhoneNo(String phoneNo) throws Exception {
		return memberDao.existedMemberByPhoneNo(phoneNo);
	}

	public boolean isDuplicateByNickname(String nickname) throws Exception {
		return memberDao.existedMemberByNickname(nickname);
	}

	public boolean login(String userName, String password) throws Exception {
		// 비회원 세션 카트에서 로그인하면 세션 카트를 로그인한 멤버의 카트DB에 담게 구현?
		Optional<Member> findOptionalMember = memberRepository.findByUserName(userName);
		if (findOptionalMember.isEmpty()) {
			throw new MemberNotFoundException(userName + " 는 존재하지않는 아이디입니다.");
		} else if (findOptionalMember.isPresent()) {
			if (password.equals(findOptionalMember.get().getPassword())) {
			} else {
				throw new PasswordMismatchException("비밀번호가 일치하지않습니다.");
			}
		}
		return true;
	}

	@Transactional
	@Override
	public void updateGrade(Member member, int gradePoint) throws Exception {
		member.setGradePoint(member.getGradePoint() + gradePoint);
		if (member.getGradePoint() <= 1000) {
			/*
			 * Rookie Bronze, Silver, Gold, Platinum, Diamond 결제 가격의 1%가 등급 포인트로 쌓임 등급 점수
			 * Rookie : 0 ~ 1000 Bronze : 1001 ~ 5000 Silver : 5001 ~ 10000 Gold : 10001 ~
			 * 20000 Platinum : 20001 ~ 35000 Diamond : 35001 ~
			 */
			member.setGrade("Rookie");
		} else if (member.getGradePoint() > 1000 && member.getGradePoint() <= 5000) {
			member.setGrade("Bronze");
		} else if (member.getGradePoint() > 5000 && member.getGradePoint() <= 10000) {
			member.setGrade("Silver");
		} else if (member.getGradePoint() > 10000 && member.getGradePoint() <= 20000) {
			member.setGrade("Gold");
		} else if (member.getGradePoint() > 20000 && member.getGradePoint() <= 35000) {
			member.setGrade("Platinum");
		} else if (member.getGradePoint() > 35000) {
			member.setGrade("Diamond");
		}
		memberDao.updatePoint(member);
		System.out.println("멤버가 안찾아짐" + member);

	}

	@Override
	public Long findIdByUsername(String username) throws Exception {
		return memberDao.findMember(username).getId();
	}

	@Override
	public boolean isMatchEmailByUserName(String userName, String email) throws Exception {
		Optional<Member> findOptionalMember = memberRepository.findByUserName(userName);
		if (findOptionalMember.isEmpty()) {
			throw new MemberNotFoundException(userName + " 는 존재하지않는 아이디입니다.");
		} else if (findOptionalMember.isPresent()) {
			if (email.equals(findOptionalMember.get().getEmail())) {
			} else {
				throw new EmailMismatchException("해당 아이디에 등록된 이메일과 다릅니다.");
			}
		}
		return true;
	}
}
