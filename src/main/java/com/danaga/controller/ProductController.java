package com.danaga.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.CategoryDto;
import com.danaga.dto.product.OtherOptionSetDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.dto.product.QueryStringDataDto;
import com.danaga.dto.product.RecentViewDto;
import com.danaga.exception.product.FoundNoObjectException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.NeedLoginException;
import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.ProductSuccessMsg;
import com.danaga.repository.product.OptionSetQueryData;
import com.danaga.service.MemberService;
import com.danaga.service.product.CategoryService;
import com.danaga.service.product.InterestService;
import com.danaga.service.product.OptionSetService;
import com.danaga.service.product.RecentViewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {

	private final OptionSetService service;
	private final CategoryService categoryService;
	private final RecentViewService recentViewService;
	private final MemberService memberService;
	private final InterestService interestService;

	// 전체상품
	@GetMapping("/product")
	public String searchProduct(Model model, HttpSession session) {
		ResponseDto<CategoryDto> categoryResponseDto = categoryService.AncestorCategories();
		List<CategoryDto> categoryList = categoryResponseDto.getData();
		if (session.getAttribute("sUserId") != null) {
			
			ResponseDto<ProductListOutputDto> responseDto = service.searchProductsForMember(// 주문수로 전체상품 정렬하여 조회
					QueryStringDataDto.builder().orderType(OptionSetQueryData.BY_ORDER_COUNT).build(),
					(String) session.getAttribute("sUserId"), 0);
			if (responseDto.getMsg().equals(ProductSuccessMsg.SEARCH_PRODUCTS)) {
				List<ProductListOutputDto> productList = responseDto.getData();
				model.addAttribute("productList", productList);
			}
		} else {
			// 검색 메인화면에 최상위 카테고리 선택할수 있게 표시
			ResponseDto<ProductListOutputDto> responseDto = service.searchProducts(// 주문수로 전체상품 정렬하여 조회
					QueryStringDataDto.builder().orderType(OptionSetQueryData.BY_ORDER_COUNT).build(), 0);
			List<ProductListOutputDto> productList = responseDto.getData();
			model.addAttribute("productList", productList);
		}
		model.addAttribute("categoryList", categoryList);
		return "product/product";
	}
	// 대분류 카테고리를 선택하고 검색메인화면으로 들어간 경우

	// 제품디테일 ++++++관련상품 20개 뽑기 옆으로 스크롤해서+++++같은 프로덕트의 다른 옵션들 뽑기
	@GetMapping("/product{optionSetId}")
	public String productDetail(HttpSession session, @PathVariable Long optionSetId, Model model)
			throws FoundNoMemberException, FoundNoOptionSetException {
		
		if (service.findById(optionSetId) == null || service.findById(optionSetId).getData().isEmpty()) {
			throw new FoundNoObjectException.FoundNoOptionSetException();
		}
		List<ProductDto> productList = service.findById(optionSetId).getData();

		// 해당 옵션셋 찾아서 뿌리기
		ResponseDto<OtherOptionSetDto> optionSets = service.showOtherOptionSets(optionSetId);
		if (optionSets.getMsg().equals(ProductExceptionMsg.FOUND_NO_PRODUCT)) {
			log.warn("this optionset(" + optionSetId + ") has no product belonged");
			throw new FoundNoOptionSetException();
		}
		service.updateViewCount(optionSetId);

		ResponseDto<ProductListOutputDto> hits = null;
		// 디테일 들어갈때 조회수도 증가
		if (session.getAttribute("sUserId") != null) {// 로그인유저일시
			String username = (String) session.getAttribute("sUserId");
			MemberResponseDto member = null;
			try {
				member = memberService.getMemberBy(username);
			} catch (Exception e) {
				log.warn("login user(username=" + session.getAttribute("sUserId")
						+ ") but failed to find in memberlist");
				e.printStackTrace();
				throw new FoundNoMemberException();
			}
			recentViewService
					.addRecentView(RecentViewDto.builder().memberId(member.getId()).optionSetId(optionSetId).build());
			// 최근본상품에 추가
			List<Boolean> isInterested;
			ResponseDto<?> interestResult = interestService.isMyInterest(optionSetId, username);
			if (interestResult.getMsg().equals(ProductSuccessMsg.IS_MY_INTEREST)) {
				isInterested = (List<Boolean>) interestResult.getData();
				model.addAttribute("isInterested", isInterested.get(0));
			}
			hits = service.displayHitProductsForMember(optionSetId, username, 0);
		} else {
			List<Long> recentOptionSetIds=null;
			if(session.getAttribute("recentviews")==null) {
			recentOptionSetIds = new ArrayList<Long>();
			}else {
			recentOptionSetIds = (List<Long>) session.getAttribute("recentviews");
			}
			recentOptionSetIds.add(optionSetId);
			session.setAttribute("recentviews", recentOptionSetIds);
			
			hits = service.displayHitProducts(optionSetId, 0);
			List<Boolean> isInterested = new ArrayList<>();
			isInterested.add(false);
			model.addAttribute("isInterested", isInterested.get(0));
		}
		// 같은 카테고리의 히트상품도 표시
		if (hits.getMsg().equals(ProductSuccessMsg.SEARCH_PRODUCTS)) {
			model.addAttribute("hits", hits.getData());
		} else if (hits.getMsg().equals(ProductExceptionMsg.FOUND_NO_CATEGORY)) {
			log.warn("this optionset(id=" + optionSetId + ") has no category");
		}
		model.addAttribute("productList", productList.get(0));
		model.addAttribute("otherOptions", optionSets.getData());
		return "product/product_detail";
	}
}
