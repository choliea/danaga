package com.danaga.service;

import org.springframework.stereotype.*;

import com.danaga.dto.*;
import com.danaga.entity.*;

public interface RefundService {
	
	//주문번호로 환불찾기
	RefundResponseDto findRefundByOrdersId(Long orderId) throws Exception; // 메인페이지에서 환불목록창 따로 파서 나오게

	//환불 요청
	RefundResponseDto saveRefund(RefundDto refundDto, Long orderId)throws Exception;// 환불페이지. 환불하시겠습니까? 네
	
}
