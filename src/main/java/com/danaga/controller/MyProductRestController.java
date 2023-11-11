package com.danaga.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.InterestDto;
import com.danaga.dto.product.RecentViewDto;
import com.danaga.exception.product.FoundNoObjectException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.NeedLoginException;
import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.ProductSuccessMsg;
import com.danaga.service.MemberService;
import com.danaga.service.product.InterestService;
import com.danaga.service.product.OptionSetService;
import com.danaga.service.product.RecentViewService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MyProductRestController {

	private final OptionSetService service;
	private final InterestService interestService;
	private final RecentViewService recentViewService;
	private final MemberService memberService;

	// 관심상품에서 하트 누르면 관심제품 추가
	@PostMapping(value = "/wishlist/{optionSetId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "관심제품 추가")
	public ResponseEntity<?> tappedHeart(@PathVariable Long optionSetId, HttpSession session)
			throws FoundNoOptionSetException, NeedLoginException, FoundNoMemberException {
		if (session.getAttribute("sUserId") == null) {
			throw new NeedLoginException();
		}
		if (service.findById(optionSetId).getMsg().equals(ProductSuccessMsg.FIND_OPTIONSET_BY_ID)) {
			try {
				String username = (String) session.getAttribute("sUserId");
				Long memberId = memberService.findIdByUsername(username);
				ResponseDto<?> response = interestService
						.clickHeart(InterestDto.builder().memberId(memberId).optionSetId(optionSetId).build());
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			} catch (Exception e) {
				throw new FoundNoMemberException();
			}
		} else {
			throw new FoundNoOptionSetException();
		}
	}

	// 관심상품에서 하트 누르면 관심제품 삭제
	@DeleteMapping(value = "/wishlist/{optionSetId}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> untappedHeart(@PathVariable Long optionSetId, HttpSession session)
			throws FoundNoOptionSetException, NeedLoginException, FoundNoMemberException {
		if (session.getAttribute("sUserId") == null) {
			throw new NeedLoginException();
		}
		if (service.findById(optionSetId).getMsg().equals(ProductSuccessMsg.FIND_OPTIONSET_BY_ID)) {
			try {
				String username = (String) session.getAttribute("sUserId");
				Long memberId = memberService.findIdByUsername(username);
				ResponseDto<?> response = interestService
						.deleteHeart(InterestDto.builder().memberId(memberId).optionSetId(optionSetId).build());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} catch (Exception e) {
				throw new FoundNoMemberException();
			}
		} else {
			throw new FoundNoOptionSetException();
		}
	}

	// 나의 최근본 상품 하나 삭제
	@DeleteMapping(value = "/recentView/{optionSetId}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> removeViewRecord(@PathVariable Long optionSetId, HttpSession session)
			throws FoundNoOptionSetException, NeedLoginException, FoundNoMemberException {
		if (session.getAttribute("sUserId") == null) {
			throw new NeedLoginException();
		}
		if (service.findById(optionSetId).getMsg().equals(ProductSuccessMsg.FIND_OPTIONSET_BY_ID)) {
			try {
				String username = (String) session.getAttribute("sUserId");
				Long memberId = memberService.findIdByUsername(username);
				ResponseDto<?> response = recentViewService
						.removeRecentView(RecentViewDto.builder().memberId(memberId).optionSetId(optionSetId).build());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} catch (Exception e) {
				throw new FoundNoMemberException();
			}
		} else {
			throw new FoundNoOptionSetException();
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
