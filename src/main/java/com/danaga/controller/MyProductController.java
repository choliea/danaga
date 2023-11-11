package com.danaga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.exception.product.FoundNoObjectException;
import com.danaga.exception.product.NeedLoginException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.service.MemberService;
import com.danaga.service.product.InterestService;
import com.danaga.service.product.RecentViewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyProductController {
	private final RecentViewService recentViewService;
	private final InterestService interestService;
	private final MemberService memberService;

	// 나의 관심상품 리스트 전체 조회
	@GetMapping("/wishlist")
//	@LoginCheck
	public String myWishList(Model model, HttpSession session) throws NeedLoginException, FoundNoMemberException {
		if (session.getAttribute("sUserId") == null || session.getAttribute("sUserId").equals("")) {
			throw new NeedLoginException();
		}
		try {
			String username = (String) session.getAttribute("sUserId");
			MemberResponseDto loginUser = memberService.getMemberBy(username);
			ResponseDto<ProductListOutputDto> resultList = interestService.myInterestingList(loginUser.getId());
			model.addAttribute("productList", resultList.getData());// memberId
			model.addAttribute("loginUser", loginUser);
			return "product/wishlist";
		} catch (Exception e) {
			e.printStackTrace();
			throw new FoundNoMemberException();
		}
	}

	// 나의 최근본 상품 전체 조회
	@GetMapping("/recentview")
	public String myRecentViews(Model model, HttpSession session) throws NeedLoginException, FoundNoMemberException {
		if (session.getAttribute("sUserId") == null || session.getAttribute("sUserId").equals("")) {
			throw new NeedLoginException();
		}
		try {
			String username = (String) session.getAttribute("sUserId");
			MemberResponseDto loginUser = memberService.getMemberBy(username);
			model.addAttribute("productList", recentViewService.myAllRecentViews(loginUser.getId()).getData());// memberId
			model.addAttribute("loginUser", loginUser);
			return "product/recent_view";
		} catch (Exception e) {
			e.printStackTrace();
			throw new FoundNoMemberException();
		}
	}

	// 나의 관심상품 리스트 전체 삭제
	@GetMapping("/wishlist_delete")
	public String deleteAllwishs(HttpSession session, Model model) throws NeedLoginException, FoundNoMemberException {
		if (session.getAttribute("sUserId") == null || session.getAttribute("sUserId").equals("")) {
			throw new NeedLoginException();
		}
		try {
			String username = (String) session.getAttribute("sUserId");
			MemberResponseDto loginUser = memberService.getMemberBy(username);
			interestService.emptyMyInterestingList(loginUser.getId());// memberId
			model.addAttribute("loginUser", loginUser);
			return "product/wishlist";
		} catch (Exception e) {
			e.printStackTrace();
			throw new FoundNoMemberException();
		}
	}

	// 나의 최근본 상품 전체 삭제
	@GetMapping("/recentview_delete")
	public String deleteViewRecords(HttpSession session, Model model) throws NeedLoginException, FoundNoMemberException {
		if (session.getAttribute("sUserId") == null || session.getAttribute("sUserId").equals("")) {
			throw new NeedLoginException();
		}
		try {
			String username = (String) session.getAttribute("sUserId");
			MemberResponseDto loginUser = memberService.getMemberBy(username);
			recentViewService.removeMyRecentViews(loginUser.getId());// memberId
			model.addAttribute("loginUser", loginUser);
			return "product/recent_view";
		} catch (Exception e) {
			e.printStackTrace();
			throw new FoundNoMemberException();
		}
	}
}
