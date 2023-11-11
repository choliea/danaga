package com.danaga.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.danaga.config.OrderStateMsg;
import com.danaga.dao.OrderDao;
import com.danaga.entity.Orders;

import jakarta.persistence.criteria.Order;

@SpringBootTest
public class OrdersRepositoryTest {
	@Autowired
	OrderDao orderDao;
	@Autowired
	OrderRepository orderRepository;

//	
//	
//	@Test
////	@Disabled
//	void insertOrderTest() {
//		
//		Orders orders = Orders.builder()
//							  .oFindNo(null)
//							  .oDesc("테스트설명")// 주문 설명 설정
//							  .oPrice(3000)// 주문 가격 설정
//							  .oFindNo("테스트주문번호")
//							  .build();
////oNo 시퀸스, oFindNo는 , oState는 디폴트값 있음
//							
//
//
//        // insertOrder 메서드 호출
//        Orders insertedOrder = orderDao.insert(orders);
//
//        // 주문이 성공적으로 저장되었는지 확인
//        assert(insertedOrder != null);
//
//        // 반환된 주문 객체의 ONo(주문 번호) 확인
//        System.out.println("Inserted Order Number: " + insertedOrder.getONo());
//    }
}