package com.danaga.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.danaga.dto.MemberResponseDto;
import com.danaga.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/member_login_form")
	public String member_login_form() {
		return "member/member_login_form";
	}


	@GetMapping("/member_join_form")
	public String member_join_form() {
		return "member/member_join_form";
	}
	@GetMapping("/member_join_complete_page")
	public String member_join_complete_page() {
		return "member/member_join_complete_page";
	}

	@GetMapping("/member_find_password_form")
	public String member_findpassword_form() {
		return "member/member_find_id_password_form";
	}

	@LoginCheck
	@GetMapping("/member_info_form")
	public String member_info_form(HttpSession session, Model model) throws Exception {
		/************** login check **************/
		/****************************************/
		String loginUser = (String) session.getAttribute("sUserId");
		MemberResponseDto member = memberService.getMemberBy(loginUser);
		model.addAttribute("loginUser", member);
		return "member/member_info_form";
	}
	@LoginCheck
	@GetMapping("/member_join_form_kakao")
	public String member_join_form_kakao(HttpSession session, Model model) throws Exception {
		/************** login check **************/
		/****************************************/
		String loginUser = (String) session.getAttribute("sUserId");
		MemberResponseDto member = memberService.getMemberBy(loginUser);
		model.addAttribute("loginUser", member);
		return "member/member_join_form_kakao";
	}


	@LoginCheck
	@GetMapping("/member_logout_action")
	public String member_logout_action(HttpSession session) {
		/************** login check **************/
		/****************************************/
		session.invalidate();

		return "redirect:index";
	}
	@LoginCheck
	@GetMapping("/member_quit_form")
	public String member_quit_form() {
		
		return "member/member_quit";
	}


	@GetMapping({ "/member_join_action", "/member_login_action", "/member_modify_action", "/member_remove_action" })
	public String user_get() {
		return "redirect:/index";
	}

}
