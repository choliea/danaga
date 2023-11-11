package com.danaga.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danaga.dto.CartDto;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.CategoryDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.dto.product.QueryStringDataDto;
import com.danaga.repository.product.OptionSetQueryData;
import com.danaga.service.CartService;
import com.danaga.service.product.OptionSetService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final CartService cartService;
	private final OptionSetService optionSetService;
	
	//메인페이지에서 카테고리별 인기상품
	//최신상품 뽑는 거 
	@RequestMapping(value = {"/index","/"})
	public String main(HttpSession session, Model model) {
		try {
			countCarts(session);
			ResponseDto<ProductListOutputDto> hits = optionSetService.searchProducts(QueryStringDataDto.builder().orderType(OptionSetQueryData.BY_VIEW_COUNT).category(CategoryDto.builder().name("전체").id(1L).build()).build(), 0);
			model.addAttribute("hits", hits.getData());
//			ResponseDto<ProductDto> responseDto = service.searchProducts(//주문수로 전체상품 정렬하여 조회
//					QueryStringDataDto.builder()
//					.orderType(OptionSetQueryData.BY_ORDER_COUNT)
//					.build());
//			List<ProductDto> productList = responseDto.getData();
//			model.addAttribute("productList",productList);
			return "index";
		} catch (Exception e) {
			// error페이지, 페이지내 에러 메세지 넘겨주기
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping("/404")
	public String error_page() {
		return "404";
	}
	
	void countCarts(HttpSession session) throws Exception {
		String sUserId = (String) session.getAttribute("sUserId");
		List<CartDto> fUserCarts = (List<CartDto>)session.getAttribute("fUserCarts");
		if (sUserId != null) {
			session.setAttribute("countCarts", cartService.countCarts(sUserId));
		} else if (fUserCarts != null) { // 비회원 일시 장바구니 리스트의 사이즈
			fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");
			session.setAttribute("countCarts",fUserCarts.size());
		}
	};
	
}
