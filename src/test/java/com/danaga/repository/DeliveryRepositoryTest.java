package com.danaga.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;

import com.danaga.entity.*;
@SpringBootTest
class DeliveryRepositoryTest {

	@Autowired
	DeliveryRepository deliveryRepository;
	
	@Test
	//@Disabled
	@Transactional
	@Rollback(false)
	void testFindDeliveryByOrdersId() {
		Delivery deliveryByOrdersId = deliveryRepository.findDeliveryByOrdersId(1L);
		System.out.println("@@@@@@@@@@@@"+deliveryByOrdersId);
		System.out.println("@@@@@@@@@@@@"+deliveryByOrdersId.getOrders());
	}

}
