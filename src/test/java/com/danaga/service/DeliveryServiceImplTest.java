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
class DeliveryServiceImplTest {
	
	@Autowired
	DeliveryService deliveryService;
	@Autowired
	OrderRepository orderRepository; 
	
	@Test
	//@Disabled
	@Transactional
	@Rollback(false)
	void testSaveDeliveryByOrdersId() {
		String name = "이름1";
		String phoneNumber = "폰넘버1";
		String address = "주소1";
		DeliveryDto deliveryDto = new DeliveryDto();
		deliveryDto.setAddress(address);
		deliveryDto.setName(name);
		deliveryDto.setPhoneNumber(phoneNumber);
		Long orderId = 1L;
		DeliveryResponseDto saveDeliveryResponseDto = deliveryService.saveDeliveryByOrdersId(deliveryDto, orderId);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+saveDeliveryResponseDto);
	}  
	
	
	
	
	
	
	
	@Test
	//@Disabled
	@Transactional
	@Rollback(false)
	void testFindDeliveryByOrdersId() throws Exception{
		DeliveryResponseDto findDeliveryResponseDto = deliveryService.findDeliveryByOrdersId(150L);
		System.out.println("##################"+findDeliveryResponseDto);
	}

}

























