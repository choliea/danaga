package com.danaga.service;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.danaga.dto.*;
import com.danaga.entity.*;

@Service
public interface DeliveryService {

	DeliveryResponseDto saveDeliveryByOrdersId(DeliveryDto deliveryDto,Long orderId); 
	
	DeliveryResponseDto findDeliveryByOrdersId(Long id)throws Exception;
//	void deleteDelivery(Long de_no) throws Exception;
}
