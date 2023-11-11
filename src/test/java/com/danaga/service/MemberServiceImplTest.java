package com.danaga.service;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.danaga.dto.MemberInsertGuestDto;
import com.danaga.dto.MemberUpdateDto;
import com.danaga.entity.Member;
import com.danaga.repository.MemberRepository;

import jakarta.transaction.Transactional;


@SpringBootTest
class MemberServiceImplTest {
	
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	
//	@Test
//	@Disabled
//	void joinMember() throws Exception {
//		memberService.joinMember(Member.builder()
//				.userName("USER11")
//				.password("password11")
//				.email("agdslfkj@naver.com")
//				.name("유저11")
//				.nickname("닉네임11")
//				.address("주소11")
//				.birthday(new Date("2023/10/15"))
//				.phoneNo("010-1123-3512")
//				.role("Member")
//				.build());
//	}
	@Test
	@Disabled
	void joinGuest() throws Exception {
		memberService.joinGuest(MemberInsertGuestDto.builder()
				.name("유저11")
				.phoneNo("010-1123-3512")
				.role("Guest")
				.build());
	}
	
	@Test
	@Disabled
	void update() throws Exception {
		memberService.updateMember(MemberUpdateDto.builder()
				.id(1L)
//				.userName("USER11")
//				.password("password11")
//				.email("agdslfkj@google.com")
//				.nickname("닉네임11")
//				.address("주소11")
//				.phoneNo("010-1123-3512")
				.build());
	}
	@Test
	@Disabled
	void delete() throws Exception {
		memberRepository.deleteById(1L);
		memberRepository.deleteById(2L);
		memberRepository.deleteById(3L);
		memberRepository.deleteById(4L);
		memberRepository.deleteById(5L);
		memberRepository.deleteById(6L);
		memberRepository.deleteById(7L);
		memberRepository.deleteById(8L);
		memberRepository.deleteById(9L);
		memberRepository.deleteById(10L);
	}
	@Test
	@Disabled
	void isDuplicate() throws Exception {
		//memberService.isDuplicate("010-1123-3512");
	}
	@Test
	@Disabled
	void login() throws Exception {
		System.out.println(memberService.login("User1", "password1"));
	}
	@Test
	//@Disabled
	@Transactional
	@Rollback(false)
	void memberOrderNull() throws Exception {
		memberService.deleteMember("Kakao3152284904");
	}

}
