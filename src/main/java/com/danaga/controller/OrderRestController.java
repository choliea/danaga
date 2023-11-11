package com.danaga.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.danaga.dto.*;
import com.danaga.dto.product.OptionSetUpdateDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.service.OrderService;
import com.danaga.service.product.OptionSetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestController {

	private final OrderService orderService;
	private final OptionSetService optionSetService;

	/*
	 * 주문validation
	 */
//	public Map order_save_action(@RequestBody OrderTotalDto orderTotalDto)

	/*
	 * 주문상세보기(회원)
	 */
	@GetMapping("/member_order_detail/{orderNo}")
	public ResponseEntity<?> memberOrderDetail(@PathVariable Long orderNo) {

		try {
			OrdersDto ordersDto = orderService.memberOrderDetail(orderNo);

			return ResponseEntity.ok(ordersDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	// 주문상태업데이트(특정주문)

	// 1.정상주문(완료)
	// OrdersDto updateStatementByNormalOrder(Long orderNo);
	@PutMapping("/updateNormal/{orderId}")
	public ResponseEntity<?> updateStatementByNormalOrder(@PathVariable(value = "orderId") Long orderId) {
		try {

			OrdersDto ordersDto = orderService.updateStatementByNormalOrder(orderId);
			return ResponseEntity.ok(ordersDto);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	/*
	 * 2.취소주문(완료)
	 */
	// OrdersDto updateStatementByCancleOrder(Long orderNo)

	@PutMapping("/updateCancel{orderId}")
	public ResponseEntity<?> updateStatementByCancleOrder(@PathVariable(value = "orderId") Long orderId) {
		try {
			OrdersDto ordersDto = orderService.updateStatementByCancleOrder(orderId);
			List<OrderItemDto> orderitemDtoList = ordersDto.getOrderItemDtoList();
			for (OrderItemDto orderItemDto : orderitemDtoList) {
				ResponseDto<?> responseDto = optionSetService.findById(orderItemDto.getOsId());
				List<ProductDto> productDtoList = (List<ProductDto>) responseDto.getData();
				OptionSetUpdateDto optionSetUpdateDto = OptionSetUpdateDto.builder().id(productDtoList.get(0).getOsId())
						.stock(productDtoList.get(0).getStock() + orderItemDto.getQty()).build();
				optionSetService.updateStock(optionSetUpdateDto);
			}
			return ResponseEntity.ok(ordersDto);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/*
	 * 3.환불주문(환불대기중)
	 */
//	 OrdersDto updateStatementByRefundOrder(Long orderNo) 
	@PutMapping("/updateClientRefund/{orderId}")
	public ResponseEntity<?> updateStatementByClientRefundOrder(@PathVariable(value = "orderId") Long orderId) {
		try {

			OrdersDto ordersDto = orderService.updateStatementByClientRefundOrder(orderId);
			return ResponseEntity.ok(ordersDto);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/*
	 * 4.환불주문(완료)
	 */
//	 OrdersDto updateStatementByRefundOrder(Long orderNo) 
	@PutMapping("/updateAdminRefund/{orderId}")
	public ResponseEntity<?> updateStatementByAdminRefundOrder(@PathVariable(value = "orderId") Long orderId) {
		try {

			OrdersDto ordersDto = orderService.updateStatementByAdminRefundOrder(orderId);
			return ResponseEntity.ok(ordersDto);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/*
	 * 4.상태리셋( 배송중→입금대기중 완료)
	 */
	// OrdersDto updateStatementByResetOrder(Long orderNo)
	@PutMapping("/updateReset/{orderId}")
	public ResponseEntity<?> updateStatementByResetOrder(@PathVariable(value = "orderId") Long orderId) {
		try {

			OrdersDto ordersDto = orderService.updateStatementByResetOrder(orderId);
			return ResponseEntity.ok(ordersDto);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/check_Stock")
	public ResponseEntity<?> orderNoStock(@RequestBody CartDto cartDto) {
		try {
			String result = "1";
			System.out.println(cartDto);
			if (optionSetService.findById(cartDto.getOptionSetId()).getData().get(0).getStock() < cartDto.getQty()) {
				throw new Exception("주문한수량보다 재고가 없습니다.");
			}
			return ResponseEntity.ok().body(result);

		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	/*
	 * stock check
	 */
	@PostMapping("/cart_check_Stock")
	public ResponseEntity<String> cartOrderNoStock(
			@RequestBody List<CartDto> cartDtos) {
		System.out.println("order 체크전 >>>>>>" + cartDtos.size());
		String result = "";
		try {
			for (int i = 0; i < cartDtos.size(); i++) {
				if (optionSetService.findById(cartDtos.get(i).getOptionSetId()).getData().get(0).getStock() >= cartDtos
						.get(i).getQty()) {
					result = "1";
				} else {
					throw new Exception("주문한수량보다 재고가 없습니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
