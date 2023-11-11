package com.danaga.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.test.annotation.Rollback;

import com.danaga.config.OrderStateMsg;
import com.danaga.dto.CartDto;
import com.danaga.dto.DeliveryDto;
import com.danaga.dto.OrderGuestDto;
import com.danaga.dto.OrdersDto;
import com.danaga.dto.OrdersProductDto;
import com.danaga.entity.Cart;
import com.danaga.entity.Delivery;
import com.danaga.entity.Member;
import com.danaga.entity.Orders;
import com.danaga.repository.DeliveryRepository;
import com.danaga.repository.OrderItemRepository;
import com.danaga.repository.OrderRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class OrderServiceImplTest {

	@Autowired
	OrderService orderService;
	@Autowired
	MemberService memberService;
	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	DeliveryRepository deliveryRepository;
	@Autowired
	CartService cartService;
	@Autowired
	OrderRepository orderRepository;
	
	/*************************************비회원*********************************/
//	@Transactional
//	@Rollback(false)
//	@Test
//	@Disabled
//	void testGuestCartOrderSave() throws Exception {
//		// List<CartCreateDto> fUserCarts, DeliveryDto deliveryDto
//		
//		
//		CartDto cartDto1 = CartDto.builder().qty(6).optionSetId(6L).build();
//		CartDto cartDto2 = CartDto.builder().qty(7).optionSetId(5L).build();
//		
//		List<CartDto> fUserCarts = new ArrayList();
//		fUserCarts.add(cartDto1);
//		fUserCarts.add(cartDto2);
//		
//		DeliveryDto deliveryDto = DeliveryDto.builder()
//				.phoneNumber("아무번호10")
//				.name("누군가10")
//				.address("서울시 관악구")
//				.detailAddress("2020-902호")
//				.postCode("123-123")
//				.build();
//		OrderGuestDto orderGuestDto = OrderGuestDto.builder()
//				.name("주문자명2")
//				.phoneNo("123-1231wewe22")
//				.build();
//		orderService.guestCartOrderSave(fUserCarts, deliveryDto,orderGuestDto);
//		
//	}
	
	@Transactional
	@Rollback(false)
	@Test
	@Disabled
	void testGuestProductOrderSave() throws Exception {
		// List<CartCreateDto> fUserCarts, DeliveryDto deliveryDto
		
		OrdersProductDto ordersProductDto = OrdersProductDto.builder()
															.optionSetId(3L)
															.orderItem_qty(3)
															.delivaryName("ddd")
															.delivaryPhoneNumber("010-3020492-2132")
															.delivaryAddress("서울시 강남구")
															.delivaryDetailAddress("빌라2034-302")
															.deliveryPostCode("444-555")
															.build();
		
		OrderGuestDto orderGuestDto = OrderGuestDto.builder()
													.name("주문자명1")
													.phoneNo("55551235---2322")
													.build();
		orderService.guestProductOrderSave(ordersProductDto,orderGuestDto);
		
	}
	
	
	@Transactional
	@Rollback(false)
	@Test
	@Disabled
	void testGuestCartSelectOrderSave() throws Exception {
		// List<CartCreateDto> fUserCarts, DeliveryDto deliveryDto
		
		DeliveryDto deliveryDto = DeliveryDto.builder()
				.phoneNumber("아무번호10")
				.name("누군가10")
				.address("서울시 관악구")
				.detailAddress("2020-902호")
				.postCode("123-12333")
				.build();
		
		
		CartDto cartDto1 = CartDto.builder().qty(6).optionSetId(1L).build();
		CartDto cartDto2 = CartDto.builder().qty(7).optionSetId(2L).build();
		
		
		List<CartDto> fUserCarts = new ArrayList();
		fUserCarts.add(cartDto1);
		fUserCarts.add(cartDto2);
		
		OrderGuestDto orderGuestDto = OrderGuestDto.builder()
													.name("주문자명1232")
													.phoneNo("123-123123232322")
													.build();
		orderService.guestCartSelectOrderSave(deliveryDto,fUserCarts,orderGuestDto);
		
	}
	
	
	@Transactional
	@Rollback(false)
	@Test
	@Disabled
	void testGuestOrderList() throws Exception {
		// List<CartCreateDto> fUserCarts, DeliveryDto deliveryDto
		
//		System.out.println("$$$$$$$$$$$$$$$"+orderService.guestOrderList(1L,"55551235---2322","주문자명1"));
		
	}
	
	
	/**************************************회원*********************************/
	
	
	@Transactional
	@Rollback(false)
	@Test
	@Disabled
	void testMemberProductOrderSave()throws Exception {
		
		System.out.println("0000000000000000000000000000000000000");
		
		OrdersProductDto ordersProductDto = OrdersProductDto.builder()
				.optionSetId(3L)
				.orderItem_qty(3)
				.delivaryName("ddd")
				.delivaryPhoneNumber("010-3020492-2132")
				.delivaryAddress("서울시 강남구")
				.delivaryDetailAddress("빌라2034-302")
				.deliveryPostCode("444-555")
				.build();
		
		orderService.memberProductOrderSave("User1",ordersProductDto);
	}
//	@Transactional
//	@Rollback(false)
//	@Test
//	@Disabled
//	void testMemberCartOrderSave()throws Exception {
//
//		
//		DeliveryDto deliveryDto = DeliveryDto.builder()
//				.phoneNumber("아무번호10")
//				.name("누군가10")
//				.address("서울시 관악구")
//				.detailAddress("2020-902호")
//				.postCode("123-12333")
//				.build();
//		
//		orderService.memberCartOrderSave("User2",deliveryDto);
//	}
	@Transactional
	@Rollback(false)
	@Test
	@Disabled
	void testMemberOrderList()throws Exception {
		
		System.out.println("-----------------------------"+orderService.memberOrderList("User1"));
		
	}

	@Transactional
	@Rollback(false)
	@Test
	@Disabled
	void testMemberCartSelectOrderSave()throws Exception {
		DeliveryDto deliveryDto = DeliveryDto.builder()
		.phoneNumber("아무번호10")
		.name("누군가10")
		.address("서울시 관악구")
		.detailAddress("2020-902호")
		.postCode("123-12333")
		.build();
		
		CartDto cartDto = CartDto.builder()
								.optionSetId(2L)
								.qty(3)
								.build();
		CartDto cartDto2 = CartDto.builder()
				.optionSetId(3L)
				.qty(3)
				.build();
		CartDto cartDto3 = CartDto.builder()
				.optionSetId(4L)
				.qty(3)
				.build();
		
		
		List<CartDto> optionSetIdArray = new ArrayList<>();
		optionSetIdArray.add(cartDto);
		optionSetIdArray.add(cartDto2);
		optionSetIdArray.add(cartDto3);
//		optionSetIdArray.add(3L);
//		optionSetIdArray.add(4L);
//		optionSetIdArray.add(5L);
		
	 System.out.println("********************************"+orderService.memberCartSelectOrderSave("User1",deliveryDto,optionSetIdArray));	
	}
	@Test
	@Disabled
	void testmemberOrderDetail()throws Exception {
		System.out.println("777777777777777777777777"+orderService.memberOrderDetail(1L)); 
	}
	@Test
	@Disabled
	void testUpdateStatementByNormalOrder()throws Exception {
		
		//System.out.println("44444444444444444444"+orderService.updateStatementByNormalOrder(5L));
//		orderService.updateStatementByNormalOrder(1L);
		orderService.updateStatementByNormalOrder(1L);
		orderService.updateStatementByNormalOrder(2L);
//		orderService.updateStatementByNormalOrder(3L);
	}
	@Test
	@Disabled
	void testUpdateStatementByCancleOrder()throws Exception {
		
		orderService.updateStatementByCancleOrder(6L);
		orderService.updateStatementByCancleOrder(19L);
	}
	
}
//	@Test
//	@Disabled
//	void testGuestProductOrderSave() throws Exception {
//		// List<CartCreateDto> fUserCarts, DeliveryDto deliveryDto
//		
//		OrdersProductDto ordersProductDto = OrdersProductDto.builder()
//															.optionSetId(3L)
//															.orderItem_qty(3)
//															.delivaryAddress("어딘가21310")
//															.delivaryName("ddd")
//															.delivaryPhoneNumber("010-3020492-2132")
//															.build();
//		
//		OrderGuestDto orderGuestDto = OrderGuestDto.builder()
//													.name("주문자명1")
//													.phoneNo("55551235---2322")
//													.build();
//		orderService.guestProductOrderSave(ordersProductDto,orderGuestDto);
//		
//	}
//	
//	
//	@Transactional
//	@Rollback(false)
//	@Test
//	@Disabled
//	void testGuestCartSelectOrderSave() throws Exception {
//		// List<CartCreateDto> fUserCarts, DeliveryDto deliveryDto
//		
//		DeliveryDto deliveryDto = DeliveryDto.builder()
//				.address("어딘가10")
//				.phoneNumber("아무번호10")
//				.name("누군가10")
//				.build();
//		
//		
//		CartDto cartDto1 = CartDto.builder().qty(6).id(1L).build();
//		CartDto cartDto2 = CartDto.builder().qty(7).id(2L).build();
//		
//		
//		List<CartDto> fUserCarts = new ArrayList();
//		fUserCarts.add(cartDto1);
//		fUserCarts.add(cartDto2);
//		
//		OrderGuestDto orderGuestDto = OrderGuestDto.builder()
//													.name("주문자명1232")
//													.phoneNo("123-123123232322")
//													.build();
//
////		orderService.guestCartSelectOrderSave(deliveryDto,fUserCarts,orderGuestDto);
//
//		
//	}
//	
//	
//	@Transactional
//	@Rollback(false)
//	@Test
//	@Disabled
//	void testGuestOrderList() throws Exception {
//		// List<CartCreateDto> fUserCarts, DeliveryDto deliveryDto
//		
//		System.out.println("$$$$$$$$$$$$$$$"+orderService.guestOrderList(1L,"123-123123232322"));
//		
//	}
//	
//	
//	/**************************************회원*********************************/
//	
//	
//	@Transactional
//	@Rollback(false)
//	@Test
//	@Disabled
//	void testMemberProductOrderSave()throws Exception {
//		
//		System.out.println("0000000000000000000000000000000000000");
//		
//		OrdersProductDto ordersDto = OrdersProductDto.builder()
//									   .delivaryAddress("dd")
//									   .delivaryName("dd")
//									   .delivaryPhoneNumber("221")
//									   .optionSetId(2L)
//									   .orderItem_qty(3)
//									   .build();
//		
//		orderService.memberProductOrderSave("User1",ordersDto);
//	}
//	@Transactional
//	@Rollback(false)
//	@Test
//	@Disabled
//	void testMemberCartOrderSave()throws Exception {
//
//		
//		DeliveryDto deliveryDto = DeliveryDto.builder()
//									.address("ff")
//									.name("ff")
//									.phoneNumber("010-3023-323232")
//									.build();
//		
//		
//		orderService.memberCartOrderSave("User2",deliveryDto);
//	}
//	@Transactional
//	@Rollback(false)
//	@Disabled
//	@Test
//	void testMemberOrderList()throws Exception {
//		
//		System.out.println("-----------------------------"+orderService.memberOrderList("User5"));
//		
//	}
//
//	@Transactional
//	@Rollback(false)
//	@Test
//	@Disabled
//	void testMemberCartSelectOrderSave()throws Exception {
//		DeliveryDto deliveryDto = DeliveryDto.builder()
//				.address("ff")
//				.name("ff")
//				.phoneNumber("010-3023-323232")
//				.build();
//		
//		CartDto cartDto = CartDto.builder()
//								.id(2L)
//								.qty(3)
//								.build();
//		CartDto cartDto2 = CartDto.builder()
//				.id(3L)
//				.qty(3)
//				.build();
//		CartDto cartDto3 = CartDto.builder()
//				.id(4L)
//				.qty(3)
//				.build();
//		
//		
//		List<CartDto> optionSetIdArray = new ArrayList<>();
//		optionSetIdArray.add(cartDto);
//		optionSetIdArray.add(cartDto2);
//		optionSetIdArray.add(cartDto3);
////		optionSetIdArray.add(3L);
////		optionSetIdArray.add(4L);
////		optionSetIdArray.add(5L);
//		
//	 System.out.println("********************************"+orderService.memberCartSelectOrderSave("User1",deliveryDto,optionSetIdArray));	
//	}
//	@Test
//	@Disabled
//	void testmemberOrderDetail()throws Exception {
//		System.out.println("777777777777777777777777"+orderService.memberOrderDetail(1L)); 
//	}
//	@Test
//	//@Disabled
//	void testUpdateStatementByNormalOrder() {
//		
//		//System.out.println("44444444444444444444"+orderService.updateStatementByNormalOrder(5L));
//		orderService.updateStatementByNormalOrder(47L);
//		orderService.updateStatementByNormalOrder(48L);
//		orderService.updateStatementByNormalOrder(49L);
//		orderService.updateStatementByNormalOrder(50L);
//		orderService.updateStatementByNormalOrder(47L);
//		orderService.updateStatementByNormalOrder(48L);
//		orderService.updateStatementByNormalOrder(49L);
//		orderService.updateStatementByNormalOrder(50L);
//	}
//	@Test
//	@Disabled
//	void testUpdateStatementByCancleOrder() {
//		
//		orderService.updateStatementByCancleOrder(6L);
//		orderService.updateStatementByCancleOrder(19L);
//	}
//	@Test
////	@Disabled
//	void testUpdateStatementByRefundOrder() {
//		
////		orderService.updateStatementByClientRefundOrder(21L);
////		orderService.updateStatementByAdminRefundOrder(23L);
//		orderService.updateStatementByAdminRefundOrder(21L);
//	}
//	@Test
//	@Disabled
//	void testUpdateStatementByResetOrder() {
//		
//		orderService.updateStatementByResetOrder(19L);
//	}
//	
//
//
//}
