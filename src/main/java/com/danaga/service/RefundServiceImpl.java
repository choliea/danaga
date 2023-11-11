package com.danaga.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.danaga.config.OrderStateMsg;
import com.danaga.dao.*;
import com.danaga.dto.*;
import com.danaga.dto.RefundResponseDto.*;
import com.danaga.entity.*;
import com.danaga.repository.*;

@Service
public class RefundServiceImpl implements RefundService {
//	private final RefundDao refundDao;

	@Autowired
	private RefundDao refundDao;
	@Autowired
	private RefundRepository refundRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDao orderDao;

//	// 환불요청 확인하기.메인페이지에서 환불목록창 따로 파서 나오게
//	public RefundResponseDto getRefundById(Long id) {
//		Refund getRefund = refundDao.selectRefund(id);
//		RefundResponseDto refundResponseDto = RefundResponseDto.toDto(getRefund);
//		return refundResponseDto;
//	}

	// 환불요청
	@Override
	@Transactional
	public RefundResponseDto saveRefund(RefundDto refundDto, Long orderId) throws Exception{
		
			Refund refund = Refund.toEntity(refundDto);
			System.out.println("@@@@@@@refund = "+ refund);
			//orderId로 orders객체를 만든 다음에 orderDaoImpl에서 
			Orders orders = orderDao.findById(orderId);
			System.out.println("@@@@@@@orders = "+ orders);
			if(orders.getStatement()==OrderStateMsg.배송완료) {
				Refund insertRefund = refundDao.insertRefund(refund, orderId);
				System.out.println("@@@@@@@insertRefund = "+ insertRefund);
				RefundResponseDto refundResponseDto = RefundResponseDto.toDto(insertRefund);
				System.out.println("@@@@@@@refundResponseDto = "+ refundResponseDto);
				orderService.updateStatementByClientRefundOrder(orderId);
				return refundResponseDto;
			} else {
				System.out.println("주문상태가 배송완료가 아닙니다.");
				return null;
			}



	}

	// 환불확인
	@Override
	@Transactional
	public RefundResponseDto findRefundByOrdersId(Long orderId) throws Exception {
		Refund findRefund = refundRepository.findRefundByOrdersId(orderId);

		if (findRefund.getOrders() != null) {
			System.out.println("해당하는 주문내역은: " + findRefund.getOrders());
		} else {
			throw new Exception("해당하는 주문내역이 없습니다.");
		}

		RefundResponseDto refudnResponseDto = RefundResponseDto.toDto(findRefund);
		return refudnResponseDto;

	}

}
