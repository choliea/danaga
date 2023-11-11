package com.danaga.service;

import java.util.List;

import com.danaga.dto.KakaoMemberUpdateDto;
import com.danaga.dto.MemberInsertGuestDto;
import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.MemberUpdateDto;
import com.danaga.entity.Member;
import com.danaga.exception.ExistedMemberByNicknameException;

import groovyjarjarantlr4.v4.parse.ANTLRParser.exceptionGroup_return;
import jakarta.transaction.Transactional;

@Transactional
public interface MemberService {
	public List<MemberResponseDto> getMembers();
	public MemberResponseDto getMemberBy(String value) throws Exception;
	public MemberResponseDto joinMember(Member member) throws Exception;
	public MemberResponseDto joinGuest(MemberInsertGuestDto memberInsertGuestDto) throws Exception;
	public MemberResponseDto updateMember(MemberUpdateDto memberUpdateDto) throws Exception;
	public MemberResponseDto updateKakaoMember(KakaoMemberUpdateDto kakaoMemberUpdateDto) throws Exception, ExistedMemberByNicknameException;
	public void deleteMember(String value) throws Exception;
	public boolean isDuplicateByUserName(String userName) throws Exception;
	public boolean isDuplicateByEmail(String email) throws Exception;
	public boolean isDuplicateByPhoneNo(String phoneNo) throws Exception;
	public boolean isDuplicateByNickname(String nickname) throws Exception;
	public boolean login(String userName, String password) throws Exception;
	public void updateGrade(Member member, int gradePoint) throws Exception;
	Long findIdByUsername(String userName) throws Exception;
	public boolean isMatchEmailByUserName(String userName, String email) throws Exception;
	
}
