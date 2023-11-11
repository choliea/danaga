package com.danaga.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;

import com.danaga.entity.*;
@SpringBootTest
class RefundRepositoryTest {
	@Autowired
	private RefundRepository refundRepository;
	
	@Test
	//@Disabled
	@Transactional
	@Rollback(false)
	void testFindRefundByOrderId() {
		Refund refundByOrdersId = refundRepository.findRefundByOrdersId(1L);
		System.out.println("@@@@@@@@@@@@"+refundByOrdersId);
	}

}
 