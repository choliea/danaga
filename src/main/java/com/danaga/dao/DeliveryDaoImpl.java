package com.danaga.dao;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.danaga.config.*;
import com.danaga.entity.*;
import com.danaga.repository.*;

@Repository
public class DeliveryDaoImpl implements DeliveryDao {

	@Autowired
	DeliveryRepository deliveryRepository;
		@Autowired
	OrderRepository orderRepository;
//	@Autowired
	OrderStateMsg orderStateMsg;



	@Override
	public Delivery insertDelivery(Delivery delivery, Long orderId) {
		Orders orders = orderRepository.findById(orderId).get();
		delivery.setOrders(orders);
		Delivery insertDelivery = deliveryRepository.save(delivery);
		return insertDelivery;
	}

//	@Override
//	public void deleteDelivery(Long id)  { // 주문상태가 입금대기중인 상태에서 배송취소 +  Orders의 oState가 취소로 변경

       // Order 엔터티 업데이트
//        Orders order = orderRepository.findByDeliveryId(Long id);
//
//        // 배송pk로
//        Delivery deleteDelivery = deliveryRepository.findById(id).get(); // 이건 배송pk로 배송객체 생성
//        deliveryRepository.delete(deleteDelivery); // 배송객체 삭제
//
//        //Order findByDeliveryId(Long id);
//        //위에거 작성 부탁하기
//        if(order!=null) { // 삭제된 배송객체와 연관된 주문객체의 oState상태를 default(입금대기중)에서 취소로 변경하기위해
//        	order.setStatement(OrderStateMsg.msg1);
//        	orderRepository.save(order); // 엔터티 업데이트
//        }

//	}
}