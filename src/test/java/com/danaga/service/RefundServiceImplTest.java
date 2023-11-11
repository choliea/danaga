package com.danaga.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;

import com.danaga.dao.*;
import com.danaga.dto.*;
import com.danaga.entity.*;
import com.danaga.repository.*;
@SpringBootTest
class RefundServiceImplTest {

	@Autowired
	private RefundService refundService;
	
    @Autowired
    private RefundDao refundDao;
	
	@Autowired
	private RefundRepository refundRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Test
	@Disabled
	@Transactional
	@Rollback(false)
	void testFindRefundByOrdersId() throws Exception{
		Long orderId = 21L;
		RefundResponseDto refundResponseDto = refundService.findRefundByOrdersId(orderId);
		
		System.out.println("##################" + refundResponseDto);
	}

	@Test
//	 @Disabled
	@Transactional
	@Rollback(false)
	void testsaveRefund() throws Exception{
		String description = "환불사유21";
		String acNo = "환불계좌번호21";
		String bankName = "국민은행";
	    String accountName = "박땡땡";
		Long orderId = 21L;
		
		RefundDto refundDto = new RefundDto();/////////////////
		refundDto.setAcNo(acNo);
		refundDto.setBankName(bankName);
		refundDto.setAccountName(accountName);
		refundDto.setAcNo(acNo);
		refundDto.setDescription(description);
		RefundResponseDto refundResponseDto = refundService.saveRefund(refundDto, orderId);
	}
}
