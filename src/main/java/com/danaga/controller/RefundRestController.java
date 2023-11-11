package com.danaga.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.danaga.dto.*;
import com.danaga.entity.*;
import com.danaga.repository.*;
import com.danaga.service.*;
import com.danaga.service.product.*;

import jakarta.servlet.http.*;
import lombok.*;

@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundRestController {
	private final OrderRepository orderRepository;
	private final MemberService memberService;
	private final RefundService refundService;
	
	@GetMapping
	public ModelAndView refund() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("refund/refund");
		return modelAndView;
	}
	
	
	//환불insert
	@PostMapping("/insert/{orderId}")
	public ResponseEntity<?> insertRefund(
											@PathVariable(value = "orderId") Long orderId,
											@RequestBody RefundDto refundDto) {
		try {
			String acNo = refundDto.getAcNo();
			String description = refundDto.getDescription();

			
			// 1. 환불 사유와 환불 계좌번호를 refundDto에서 가져온다.
			
			RefundResponseDto refundResponseDto = refundService.saveRefund(refundDto, orderId);
			return ResponseEntity.status(HttpStatus.CREATED).body(refundResponseDto);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("#######################");
			 return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	//환불 select
	@GetMapping("/select/{orderId}")
	public ResponseEntity<?> findRefundByOrderId(@PathVariable(value = "orderId") Long orderId) {
		try {
			RefundResponseDto refundResponseDto = refundService.findRefundByOrdersId(orderId);
			return ResponseEntity.status(HttpStatus.CREATED).body(refundResponseDto);		
			} catch(Exception e) {
			e.printStackTrace();
			 return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	

}












