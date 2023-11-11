package com.danaga.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.InterestDto;
import com.danaga.dto.product.QueryStringDataDto;
import com.danaga.exception.product.FoundNoObjectException;
import com.danaga.exception.product.NeedLoginException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.ProductSuccessMsg;
import com.danaga.service.MemberService;
import com.danaga.service.product.CategoryService;
import com.danaga.service.product.InterestService;
import com.danaga.service.product.OptionSetService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductRestController {

	private final OptionSetService service;
	private final MemberService memberService;
	private final InterestService interestService;
	private final CategoryService categoryService;

	// 제품디테일에서 하트 누르면 관심제품 추가
	@PostMapping("/heart/{optionSetId}")
	public ResponseEntity<?> tapHeartInDetail(HttpSession session, @PathVariable Long optionSetId)
			throws FoundNoMemberException, FoundNoOptionSetException, NeedLoginException {
		ResponseDto<?> responseBody = new ResponseDto<>();
		if (session.getAttribute("sUserId") == null) {
			throw new NeedLoginException();
		}
		if (service.findById(optionSetId).getMsg().equals(ProductSuccessMsg.FIND_OPTIONSET_BY_ID)) {
			try {
				String username = (String) session.getAttribute("sUserId");
				MemberResponseDto member = memberService.getMemberBy(username);
				responseBody = interestService
						.clickHeart(InterestDto.builder().memberId(member.getId()).optionSetId(optionSetId).build());
				return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
			} catch (Exception e) {
				throw new FoundNoObjectException.FoundNoMemberException();
			}
		} else {
			throw new FoundNoObjectException.FoundNoOptionSetException();
		}
	}

	// 제품디테일에서 하트 누르면 관심제품 삭제
	@DeleteMapping("/heart/{optionSetId}")
	public ResponseEntity<?> untapHeartInDetail(HttpSession session, @PathVariable Long optionSetId)
			throws FoundNoMemberException, FoundNoOptionSetException, NeedLoginException {
		ResponseDto<?> responseBody = new ResponseDto<>();
		if (session.getAttribute("sUserId") == null) {
			throw new NeedLoginException();
		}
		if (service.findById(optionSetId).getMsg().equals(ProductSuccessMsg.FIND_OPTIONSET_BY_ID)) {
			try {
				String username = (String) session.getAttribute("sUserId");
				MemberResponseDto member = memberService.getMemberBy(username);
				responseBody = interestService
						.deleteHeart(InterestDto.builder().memberId(member.getId()).optionSetId(optionSetId).build());
				return ResponseEntity.status(HttpStatus.OK).body(responseBody);
			} catch (Exception e) {
				throw new FoundNoObjectException.FoundNoMemberException();
			}
		} else {
			throw new FoundNoObjectException.FoundNoOptionSetException();
		}
	}

	// 카테고리의 상위, 자식들 조회
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<?> chooseCategory(@PathVariable Long categoryId) {
		ResponseDto<?> response = categoryService.categoryFamily(categoryId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 최하위 선택시 선택가능한 옵션명, 옵션값 반환
	@GetMapping("/category/options/{categoryId}")
	public ResponseEntity<?> showOptionFilter(@PathVariable Long categoryId) {
		ResponseDto<?> response = new ResponseDto<>();
		// 만약 부모 id이면 showAllOptionNameValue
		// 아니면 showOptionNameValue
		if (categoryService.isYoungest(categoryId)) {
			response = service.showOptionNameValues(categoryId);
		} else {
			response = service.showAllOptionNameValues(categoryId);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 조건에 해당하는 리스트 전체 조회
	// datadto에서 category가 부모 카테고리(전체)이면 어떡할지 여기서 처리
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchResult(@RequestBody QueryStringDataDto filterDto, HttpSession session) {
		System.out.println("restcontroller의 filterdto 검색조건>>>>>>>>>>>" + filterDto);
		if (session.getAttribute("sUserId") == null) {
			ResponseDto<?> response = service.searchProducts(filterDto, filterDto.getFirstResult());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			ResponseDto<?> response = service.searchProductsForMember(filterDto,
					(String) session.getAttribute("sUserId"), filterDto.getFirstResult());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}
	@ResponseBody
	@ExceptionHandler(value = {NeedLoginException.class,FoundNoOptionSetException.class,FoundNoMemberException.class,MethodArgumentNotValidException.class})
	protected ResponseEntity<?> defaultRestException(Exception e) {
		 ProductExceptionMsg errorMsg=null;
		    if (e instanceof NeedLoginException) {
		        errorMsg = ((NeedLoginException) e).getMsg();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().msg(errorMsg).build());
		    } else if (e instanceof FoundNoObjectException.FoundNoMemberException) {
		        errorMsg = ((FoundNoObjectException.FoundNoMemberException) e).getMsg();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().msg(errorMsg).build());
		    }  else if (e instanceof FoundNoOptionSetException) {
		        errorMsg = ((FoundNoOptionSetException) e).getMsg();
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().msg(errorMsg).build());
		    } else if (e instanceof MethodArgumentNotValidException) {
		        errorMsg = ProductExceptionMsg.WRONG_PARAMETER;
		    } 
		    return ResponseEntity.badRequest().body(ResponseDto.builder().msg(errorMsg).build());
		}
}