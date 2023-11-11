package com.danaga.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.danaga.dao.*;
import com.danaga.dto.*;
import com.danaga.entity.*;
import com.danaga.repository.*;

@Service
public class DeliveryServiceImpl implements DeliveryService{

	@Autowired
	private DeliveryDao deliveryDao;
	
	@Autowired 
	private DeliveryRepository deliveryRepository;
	

	//배송신청
	@Override
	@Transactional
	public DeliveryResponseDto saveDeliveryByOrdersId(DeliveryDto deliveryDto, Long orderId) {
		Delivery delivery = Delivery.toEntity(deliveryDto);	
		Delivery createdDelivery = deliveryDao.insertDelivery(delivery, orderId);
		return DeliveryResponseDto.toDto(createdDelivery);
	}
	
	//배송확인
	@Override
	@Transactional
	public DeliveryResponseDto findDeliveryByOrdersId(Long id) throws Exception {
		Delivery findDelivery = deliveryRepository.findDeliveryByOrdersId(id);
		if (findDelivery.getOrders() != null) {//order가 존재하면
		    System.out.println("@@@@@@@@@@@@" + findDelivery.getOrders());
		} else {
			throw new Exception("해당하는 주문내역이 없습니다.");
		}
		return DeliveryResponseDto.toDto(findDelivery);
	}
	
}
