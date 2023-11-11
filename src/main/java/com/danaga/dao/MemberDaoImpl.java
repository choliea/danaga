package com.danaga.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.danaga.entity.Member;
import com.danaga.exception.MemberNotFoundException;
import com.danaga.repository.MemberRepository;

@Repository
public class MemberDaoImpl implements MemberDao {
	@Autowired
	private MemberRepository memberRepository;

	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	public Member findMember(String value) throws Exception, MemberNotFoundException {
		if (value.contains("@")) {
			if (memberRepository.findByEmail(value).isPresent()) {
				return memberRepository.findByEmail(value).get();
			}
			throw new MemberNotFoundException("해당 이메일로 찾을 수 없습니다");
		} else if (value.contains("-")) {
			if (memberRepository.findByPhoneNo(value).isPresent()) {
				return memberRepository.findByPhoneNo(value).get();
			}
			throw new MemberNotFoundException("해당 번호로 찾을 수 없습니다");
		} else {
			if (memberRepository.findByUserName(value).isPresent()) {
				return memberRepository.findByUserName(value).get();
			}
			throw new MemberNotFoundException("해당 아이디로 찾을 수 없습니다");
		}
	}
	

	public Member insert(Member member) {
		return memberRepository.save(member);
	}

	public Member updatePoint(Member updateMember) throws Exception {
		Optional<Member> findOptionalMember = memberRepository.findById(updateMember.getId());
		Member updatedMember = null;
		if (findOptionalMember.isPresent()) {
			Member member = findOptionalMember.get();
			member.setGrade(updateMember.getGrade());
			member.setGradePoint(updateMember.getGradePoint());
			updatedMember = memberRepository.save(member);
		} else {
			throw new MemberNotFoundException("존재하지 않는 회원입니다");
		}
		return updatedMember;
	}
	public Member updateGuestPhoneNo(Member updateMember) throws Exception {
		Optional<Member> findOptionalMember = memberRepository.findById(updateMember.getId());
		Member updatedMember = null;
		if (findOptionalMember.isPresent()) {
			Member member = findOptionalMember.get();
			member.setPhoneNo(updateMember.getPhoneNo());
			updatedMember = memberRepository.save(member);
		} else {
			throw new MemberNotFoundException("존재하지 않는 회원입니다");
		}
		return updatedMember;
	}
	public Member updateGuestEmail(Member updateMember) throws Exception {
		Optional<Member> findOptionalMember = memberRepository.findById(updateMember.getId());
		Member updatedMember = null;
		if (findOptionalMember.isPresent()) {
			Member member = findOptionalMember.get();
			member.setEmail(updateMember.getEmail());
			updatedMember = memberRepository.save(member);
		} else {
			throw new MemberNotFoundException("존재하지 않는 회원입니다");
		}
		return updatedMember;
	}
	public Member update(Member updateMember) throws Exception {
		Optional<Member> findOptionalMember = memberRepository.findById(updateMember.getId());
		Member updatedMember = null;
		if (findOptionalMember.isPresent()) {
			Member member = findOptionalMember.get();
			member.setUserName(updateMember.getUserName());
			member.setPassword(updateMember.getPassword());
			member.setNickname(updateMember.getNickname());
			member.setPostCode(updateMember.getPostCode());
			member.setAddress(updateMember.getAddress());
			member.setDetailAddress(updateMember.getDetailAddress());
			member.setRole(updateMember.getRole());
			member.setGrade(updateMember.getGrade());
			member.setGradePoint(updateMember.getGradePoint());
			updatedMember = memberRepository.save(member);
		} else {
			throw new MemberNotFoundException("존재하지 않는 회원입니다");
		}
		return updatedMember;
	}

	public void delete(String value) throws Exception, MemberNotFoundException {
		if (memberRepository.findByUserName(value).isPresent()) {
			memberRepository.delete(memberRepository.findByUserName(value).get());
		} else if (memberRepository.findByEmail(value).isPresent()) {
			memberRepository.delete(memberRepository.findByEmail(value).get());
		} else {
			throw new MemberNotFoundException("존재하지 않는 회원입니다");
		}
	}

	public boolean existedMemberByUserName(String userName) throws Exception {
		if (memberRepository.findByUserName(userName).isPresent()) {
			return true;
		}
		return false;
	}
	public boolean existedMemberByEmail(String email) throws Exception {
		if (memberRepository.findByEmail(email).isPresent()) {
			return true;
		}
		return false;
	}
	public boolean existedMemberByPhoneNo(String phoneNo) throws Exception {
		if (memberRepository.findByPhoneNo(phoneNo).isPresent()) {
			return true;
		}
		return false;
	}
	public boolean existedMemberByNickname(String nickname) throws Exception {
			if (memberRepository.findByNickname(nickname).isPresent()) {
				return true;
			}
		return false;
	}

}
