package com.danaga.dao;

import java.util.List;

import com.danaga.entity.Member;

public interface MemberDao {
	public List<Member> findMembers();
	public Member findMember(String value) throws Exception;
	public Member insert(Member member) throws Exception;
	public Member updatePoint(Member updateMember) throws Exception;
	public Member update(Member updateMember) throws Exception;
	public void delete(String memberId) throws Exception;
	public boolean existedMemberByUserName(String userName) throws Exception;
	public boolean existedMemberByEmail(String email) throws Exception;
	public boolean existedMemberByPhoneNo(String phoneNo) throws Exception;
	public boolean existedMemberByNickname(String nickname) throws Exception;
	public Member updateGuestPhoneNo(Member updateMember) throws Exception;
	public Member updateGuestEmail(Member updateMember) throws Exception;
	}
