package com.danaga.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danaga.dto.CartDto;
import com.danaga.dto.CartElseOptionsetDto;
import com.danaga.dto.FUserCartResponseDto;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.SUserCartResponseDto;
import com.danaga.dto.product.OtherOptionSetDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.service.CartService;
import com.danaga.service.product.OptionSetService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final OptionSetService optionSetService;

	static String sUserId; // 로그인 유저 아이디
	static List<CartDto> fUserCarts; // 비회원 장바구니(세션)

	@GetMapping("/cart_list")
	public String findCarts(HttpSession session, Model model) throws Exception {
		sUserId = (String) session.getAttribute("sUserId");
		fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");
		if (sUserId != null) { // 회원
			List<SUserCartResponseDto> carts = cartService.findsUserCartList(sUserId);
			if (carts != null) {
				for (int i = 0; i < carts.size(); i++) {
					ResponseDto<OtherOptionSetDto> findLists = optionSetService
							.showOtherOptionSets(carts.get(i).getOsId());
					carts.get(i).setElseOptionSets(findLists.getData().stream().map(t -> new CartElseOptionsetDto(t))
							.collect(Collectors.toList()));
				}
				int a = (int) (Math.random() * carts.size());
				session.setAttribute("countCarts", carts.size());
				if(!carts.isEmpty()) {
					model.addAttribute("hits",
							optionSetService.displayHitProductsForMember(carts.get(a).getOsId(), sUserId, 0).getData());
				}
			}
			model.addAttribute("cart", carts);
		} else {// 비회원
			List<FUserCartResponseDto> responseDto = new ArrayList<>();
			if (fUserCarts != null ) {
				for (int i = 0; i < fUserCarts.size(); i++) {
					responseDto.add(cartService.findfUserCartList(fUserCarts.get(i)));
				}
				for (int i = 0; i < responseDto.size(); i++) {
					ResponseDto<OtherOptionSetDto> findLists = optionSetService
							.showOtherOptionSets(responseDto.get(i).getOsId());
					responseDto.get(i).setElseOptionSets(findLists.getData().stream()
							.map(t -> new CartElseOptionsetDto(t)).collect(Collectors.toList()));
				}
				int a = (int) (Math.random() * responseDto.size());
				session.setAttribute("countCarts", responseDto.size());
				if (!responseDto.isEmpty()) {
					model.addAttribute("hits",
							optionSetService.displayHitProducts(responseDto.get(a).getOsId(), 0).getData());
				}
				model.addAttribute("cart", responseDto);
			}
		}
		return "cart/cart_form";
	}

}