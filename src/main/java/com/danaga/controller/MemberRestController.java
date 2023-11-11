package com.danaga.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danaga.dto.CartDto;
import com.danaga.dto.KakaoMemberUpdateDto;
import com.danaga.dto.MemberFindDto;
import com.danaga.dto.MemberLoginDto;
import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.MemberUpdateDto;
import com.danaga.dto.product.RecentViewDto;
import com.danaga.entity.Member;
import com.danaga.exception.EmailMismatchException;
import com.danaga.exception.ExistedMemberByEmailException;
import com.danaga.exception.ExistedMemberByNicknameException;
import com.danaga.exception.ExistedMemberByPhoneNoException;
import com.danaga.exception.ExistedMemberByUserNameException;
import com.danaga.exception.MemberNotFoundException;
import com.danaga.exception.PasswordMismatchException;
import com.danaga.service.CartService;
import com.danaga.service.MemberService;
import com.danaga.service.product.RecentViewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberRestController {
	private final MemberService memberService;
	private final CartService cartService;
	private final RecentViewService recentViewService;

	@PostMapping(value = "/findid_rest", produces = "application/json;charset=UTF-8")
	public Map member_findid_action_rest(@RequestBody MemberFindDto memberFindDto, HttpSession session)
			throws Exception {
		HashMap map = new HashMap<>();
		//MemberResponseDto memberResponseDto = MemberResponseDto.builder().userName(userName).password(password).build();
		int result = 1;
		
		try {
			memberService.getMemberBy(memberFindDto.getEmail());
		} catch (MemberNotFoundException e) {
			result = 0;
			map.put("result", result);
			map.put("msg", memberFindDto.getEmail() + "는 등록되지 않은 이메일입니다.");
			return map;
		}
		
		map.put("result", result);
		return map;
	}
	@PostMapping(value = "/findpass_rest", produces = "application/json;charset=UTF-8")
	public Map member_findpass_action_rest(@RequestBody MemberFindDto memberFindDto, HttpSession session)
			throws Exception {
		HashMap map = new HashMap<>();
		//MemberResponseDto memberResponseDto = MemberResponseDto.builder().userName(userName).password(password).build();
		int result = 2;
		
		try {
			memberService.isMatchEmailByUserName(memberFindDto.getUserName(), memberFindDto.getEmail());
		} catch (MemberNotFoundException e) {
			result = 0;
			map.put("result", result);
			map.put("msg", memberFindDto.getUserName() + "는 존재하지 않는 아이디입니다.");
			return map;
		} catch (EmailMismatchException e) {
			result = 1;
			map.put("result", result);
			map.put("msg", "해당 아이디에 등록된 이메일이 아닙니다.");
			return map;
		}
		
		map.put("result", result);
		return map;
	}
	@PostMapping(value = "/login_rest", produces = "application/json;charset=UTF-8")
	public Map member_login_action_rest(@RequestBody MemberLoginDto memberLoginDto, HttpSession session)
			throws Exception {
		HashMap map = new HashMap<>();
		//MemberResponseDto memberResponseDto = MemberResponseDto.builder().userName(userName).password(password).build();
		int result = 2;

		try {
			memberService.login(memberLoginDto.getUserName(), memberLoginDto.getPassword());
		} catch (MemberNotFoundException e) {
			result = 0;
			map.put("result", result);
			map.put("member", memberLoginDto);
			return map;
		} catch (PasswordMismatchException e) {
			result = 1;
			map.put("result", result);
			return map;
		}
		MemberResponseDto loginUser = memberService.getMemberBy(memberLoginDto.getUserName());
		/********* 비회원으로 카트 담고 로그인시 회원 카트 DB insert **********/
		List<CartDto> fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");
		session.setAttribute("sUserId", loginUser.getUserName());
		if (fUserCarts != null) {
			// 로그인 + 세션 장바구니 존재
			for (int i = 0; i < fUserCarts.size(); i++) {
				cartService.addCart(fUserCarts.get(i), loginUser.getUserName());
				// 세션 -> db 로 데이타 인서트 후 세션 데이타 초기화 후 세션카트 카운트
			}
			session.setAttribute("fUserCarts", null);
			session.setAttribute("countCarts", cartService.countCarts(loginUser.getUserName()));
		}
		/***************************************************************************************/
		/********* 비회원으로 최근 본 상품 로그인시 회원 정보에 insert **********/
		if(session.getAttribute("recentviews")!=null) {
		List<Long> recentOptionSetIds= (List<Long>) session.getAttribute("recentviews");
		for (Long optionSetId : recentOptionSetIds) {
			recentViewService.addRecentView(RecentViewDto.builder()
					.optionSetId(optionSetId).memberId(loginUser.getId()).build());
		}
		session.removeAttribute("recentviews");
		}
		/***************************************************************************************/
		if (loginUser.getRole().equals("Admin")) {
			session.setAttribute("role", loginUser.getRole());
		}

		map.put("result", result);
		return map;
	}

	@PostMapping("/join_rest")
	public Map member_join_action(@RequestBody Member member) throws Exception {
		HashMap map = new HashMap<>();
		// MemberResponseDto memberResponseDto =
		// MemberResponseDto.builder().userName(userName).password(password).build();
		int result = 5;

		try {
			memberService.joinMember(member);

		} catch (ExistedMemberByUserNameException e) {
			result = 1;
			map.put("result", result);
			map.put("msg", member.getUserName() + "는 사용중인 아이디입니다.");
			return map;
		} catch (ExistedMemberByEmailException e) {
			result = 2;
			map.put("result", result);
			map.put("msg", member.getEmail() + "는 사용중인 이메일입니다.");
			return map;
		} catch (ExistedMemberByPhoneNoException e) {
			result = 3;
			map.put("result", result);
			map.put("msg", member.getPhoneNo() + "는 사용중인 번호입니다.");
			return map;
		} catch (ExistedMemberByNicknameException e) {
			result = 4;
			map.put("result", result);
			map.put("msg", member.getNickname() + "는 사용중인 닉네임입니다.");
			return map;
		}
		map.put("result", result);
		return map;
	}
	@PostMapping("/join_rest_kakao")
	public Map member_join_action_kakao(@RequestBody Member member, HttpSession session) throws Exception {
		HashMap map = new HashMap<>();
		// MemberResponseDto memberResponseDto =
		// MemberResponseDto.builder().userName(userName).password(password).build();
		int result = 3;
		try {
			String sUserId = (String) session.getAttribute("sUserId");
			Long sUserLongId = memberService.getMemberBy(sUserId).getId();
			member.setId(sUserLongId);
			memberService.updateKakaoMember(KakaoMemberUpdateDto.toDto(member));
		}catch (ExistedMemberByUserNameException e) {
			result = 1;
			map.put("result", result);
			map.put("msg", member.getUserName() + "는 사용중인 아이디입니다.");
			return map;
		} catch (ExistedMemberByNicknameException e) {
			result = 2;
			map.put("result", result);
			map.put("msg", member.getNickname() + "는 사용중인 닉네임입니다.");
			return map;
		}
		map.put("result", result);
		session.removeAttribute("role");
		return map;
	}

	@LoginCheck
	@PutMapping(value = "/modify_action_rest", produces = "application/json;charset=UTF-8")
	public Map member_modify_action(@RequestBody MemberUpdateDto memberUpdateDto, HttpSession session)
			throws Exception {
		HashMap map = new HashMap<>();
		int result = 2;
		try {
			String sUserId = (String) session.getAttribute("sUserId");
			Long sUserLongId = memberService.getMemberBy(sUserId).getId();
			memberUpdateDto.setId(sUserLongId);
			memberService.updateMember(memberUpdateDto);
		} catch (ExistedMemberByNicknameException e) {
			result = 1;
			map.put("result", result);
			map.put("msg", memberUpdateDto.getNickname() + "는 사용중인 닉네임입니다.");
			return map;
		}
		map.put("result", result);
		return map;
	}
	@LoginCheck
	@DeleteMapping(value = "/delete_action_rest", produces = "application/json;charset=UTF-8")
	public Map delete_action_rest(@RequestBody MemberLoginDto memberLoginDto, HttpSession session) throws Exception {
		HashMap map = new HashMap<>();
		int result = 2;
		try {
			String sUserId = (String) session.getAttribute("sUserId");
			MemberResponseDto member = memberService.getMemberBy(sUserId);
			if (member.getPassword().equals(memberLoginDto.getPassword())) {
				memberService.deleteMember(member.getUserName());
				session.invalidate();
			} else {
				throw new PasswordMismatchException("비밀번호가 일치하지않습니다.");
			}
		} catch (PasswordMismatchException e) {
			result = 1;
			map.put("result", result);
			map.put("msg", "비밀번호가 일치하지않습니다.");
			return map;
		}
		map.put("result", result);
		return map;
	}


}
