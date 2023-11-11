package com.danaga.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.danaga.config.OrderStateMsg;
import com.danaga.dao.MemberDao;
import com.danaga.dao.OrderDao;
import com.danaga.dao.product.OptionSetDao;
import com.danaga.dto.*;
import com.danaga.dto.product.ProductDto;
import com.danaga.entity.*;
import com.danaga.repository.CartRepository;
import com.danaga.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {

	private final CartRepository cartRepository;
	private final OrderItemRepository orderItemRepository;
	private final MemberService memberService;
	private final OptionSetDao optionSetDao;
	private final OrderDao orderDao;
	private final CartService cartService;
	private final MemberDao memberDao;

	/******************************************************
	 * 비회원
	 ******************************************************************/
	/*
	 * 카트에서 주문(비회원)
	 */
//	@Transactional
//	public OrdersDto guestCartOrderSave(List<CartDto> fUserCarts, DeliveryDto deliveryDto, OrderGuestDto orderGuestDto)  throws Exception{
//		//CartCreateDto에는 int qty와 OptionSet optionSet이 들어있다.
//	
//	    int o_tot_price = 0;
//	    int oi_tot_count = 0;
//	    List<OrderItem> orderItemList = new ArrayList<>();
//	   
//	    
//	    for (CartDto cartDto : fUserCarts) {//카트Dto로 부터 orderItem을 만들어서 orderItemList를만들려고
//	    	OptionSet optionSet =optionSetDao.findById(cartDto.getOptionSetId());
//	    	
//	    	o_tot_price+=optionSet.getTotalPrice()*cartDto.getQty();//주문의 요소 중 하나임
//	    	oi_tot_count+=cartDto.getQty();//주문Desc만드려고
//	    	
//	    	
//	    	 OrderItem orderItem = orderItemRepository.save(OrderItem.builder()
//		               .qty(cartDto.getQty())
//		               .optionSet(optionSet)
//		               .build());
//	    	 
//	         orderItemList.add(orderItem);
//	    	
//	    }
//	    
//	    OptionSet optionSet= optionSetDao.findById(orderItemList.get(0).getOptionSet().getId());
//		
//	    String o_desc = optionSet.getProduct().getName() + "외" + (oi_tot_count - 1) + "개";
//	       if (oi_tot_count == 1) {
//	    	   o_desc = optionSet.getProduct().getName();
//	       }
//		
//		   	MemberResponseDto memberResponseDto =memberService.joinGuest(MemberInsertGuestDto.builder()
//										   							   .name(orderGuestDto.getName())
//										   							   .phoneNo(orderGuestDto.getPhoneNo())
//										   							   .userName(orderGuestDto.getName()+orderGuestDto.getPhoneNo())
//										   							   .role("Guest")
//										   							   .build());
//		   	Member member= Member.toResponseEntity(memberResponseDto);
//		   	
//		      Delivery delivery = Delivery.builder()
//	  					.name(deliveryDto.getName())
//	  					.phoneNumber(deliveryDto.getPhoneNumber())
//	  					.address(deliveryDto.getAddress())
//	  					.build();
//		   	
//		     Orders orders = orderDao.save(Orders.builder()
//                      .description(o_desc)
//                      .price(o_tot_price)
//                      .statement(OrderStateMsg.입금대기중)
//                      .member(member)
//                      .orderItems(orderItemList)
//                      .delivery(delivery)
//                      .build());
//		     
//		     member.setUserName(orders.getId()+member.getPhoneNo());
//		     memberDao.insert(member);
//		     
//		      for (OrderItem orderItem : orderItemList) {
//					orderItem.setOrders(orders);
//				}
//		      delivery.setOrders(orders);
//		      
//
//		      return OrdersDto.orderDto(orders);
//	}

	/*
	 * 상품에서 직접주문(비회원)
	 */
	@Transactional
	public OrdersDto guestProductOrderSave(OrdersProductDto ordersProductDto, OrderGuestDto orderGuestDto)
			throws Exception {

		OptionSet optionSet = optionSetDao.findById(ordersProductDto.getOptionSetId());// 상품 찾고
		List<OrderItem> orderItemList = new ArrayList<>();

		OrderItem orderItem = OrderItem.builder().qty(ordersProductDto.getOrderItem_qty()).optionSet(optionSet).build();// orderItem찾음

		orderItemList.add(orderItem);// 상품에서 직접 주문하는거니까 orderItem하나만 들어간다

		Delivery delivery = Delivery.builder().name(ordersProductDto.getDelivaryName())
				.phoneNumber(ordersProductDto.getDelivaryPhoneNumber()).address(ordersProductDto.getDelivaryAddress())
				.detailAddress(ordersProductDto.getDelivaryDetailAddress())
				.postCode(ordersProductDto.getDeliveryPostCode()).build();

		System.out.println("@@@@@@@@@@@@@@@delivery= " + delivery);

		MemberResponseDto memberResponseDto = memberService.joinGuest(MemberInsertGuestDto.builder()
				.name(orderGuestDto.getName()).phoneNo(orderGuestDto.getPhoneNo()).email(orderGuestDto.getEmail()).role("Guest").build());
		Member member = Member.toResponseEntity(memberResponseDto);

		Orders orders = orderDao.save(Orders.builder().description(optionSet.getProduct().getName())
				.price(optionSet.getProduct().getPrice() * ordersProductDto.getOrderItem_qty())
				.statement(OrderStateMsg.입금대기중).member(member).delivery(delivery).orderItems(orderItemList).build());

		delivery.setOrders(orders);
		orderItem.setOrders(orders); // transactional

		return OrdersDto.orderDto(orders);

	}

	/*
	 * cart에서 선택주문(비회원)
	 */
	@Transactional
	public OrdersDto guestCartSelectOrderSave(DeliveryDto deliveryDto, List<CartDto> fUserCarts,
			OrderGuestDto orderGuestDto) throws Exception {

		ArrayList<OrderItem> orderItemList = new ArrayList<>();

		int o_tot_price = 0;
		int oi_tot_count = 0;

		MemberResponseDto memberResponseDto = memberService.joinGuest(MemberInsertGuestDto.builder()
				.name(orderGuestDto.getName())
				.phoneNo(orderGuestDto.getPhoneNo())
				.email(orderGuestDto.getEmail())
				.role("Guest")
				.build());
		Member member = Member.toResponseEntity(memberResponseDto);
		System.out.println("##########################"+member);

		for (int i = 0; i < fUserCarts.size(); i++) {

			CartDto cartDto = fUserCarts.get(i);
			OptionSet optionSet = optionSetDao.findById(cartDto.getOptionSetId());

			o_tot_price += (optionSet.getTotalPrice()) * (cartDto.getQty());
			oi_tot_count += cartDto.getQty();

			OrderItem inputOIEntity = OrderItem.builder().qty(cartDto.getQty()).optionSet(optionSet).build();

			orderItemList.add(inputOIEntity);
		}

		OptionSet optionSet = optionSetDao.findById(orderItemList.get(0).getOptionSet().getId());

		String o_desc = optionSet.getProduct().getName() + "외" + (oi_tot_count - 1) + "개";

		if (oi_tot_count == 1) {
			o_desc = optionSet.getProduct().getName();
		}

		Delivery delivery = Delivery.builder().name(deliveryDto.getName()).phoneNumber(deliveryDto.getPhoneNumber())
				.address(deliveryDto.getAddress()).detailAddress(deliveryDto.getDetailAddress())
				.postCode(deliveryDto.getPostCode()).build();
		System.out.println("@@@@@@@@@@@@@@@@@@delivery: " + delivery);

		Orders orders = Orders.builder().description(o_desc).price(o_tot_price).statement(OrderStateMsg.입금대기중)
				.member(member).delivery(delivery).orderItems(orderItemList).build();
		System.out.println("@@@@@@@@@@@@@@@@@@orders: " + orders);

		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrders(orders);
		}
		delivery.setOrders(orders);
		orders.setMember(member);
		Orders realOrders = orderDao.save(orders);// 마지막에 세이브해야되는듯
		System.out.println("@@@@@@@@@@@@@@@@@@delivery: " + delivery);
		System.out.println("@@@@@@@@@@@@@@@@@@orderItemList: " + orderItemList);
		System.out.println("@@@@@@@@@@@@@@@@@@real orders: " + realOrders);

		return OrdersDto.orderDto(orders);
	}

	/*
	 * 주문+주문아이템 목록(비회원)
	 */

	@Transactional
	public List<OrdersDto> guestOrderList(Long orderNo, String phoneNumber) throws Exception {
		if (phoneNumber.equals(memberService.getMemberBy(phoneNumber).getPhoneNo())) {
			if (orderNo == orderDao.findById(orderNo).getId()) {
				List<Orders> orderList = new ArrayList<>();
				orderList.add(orderDao.findById(orderNo));
				List<OrdersDto> ordersDtoList = new ArrayList<>();
				for (Orders orders : orderList) {
					OrdersDto ordersDto = OrdersDto.orderDto(orders);
					ordersDtoList.add(ordersDto);
				}
				return ordersDtoList;
			}
			throw new Exception("일치하는 주문번호가 없습니다.");
		}
		throw new Exception("일치하는 정보가 없습니다.");
	}

	/*
	 * 주문상세보기(회원)
	 */
	@Transactional
	public OrdersDto guestOrderDetail(Long orderNo, String phoneNo) throws Exception {

		Orders orders = orderDao.findOrdersByIdAndPhoneNo(orderNo, phoneNo);

		return OrdersDto.orderDto(orders);
	}

	/**************************************************************
	 * 회원
	 ******************************************************************/

	/*
	 * 상품에서 직접주문(회원)
	 */
	@Transactional
	public OrdersDto memberProductOrderSave(String sUserId, OrdersProductDto ordersProductDto) throws Exception {

		OptionSet optionSet = optionSetDao.findById(ordersProductDto.getOptionSetId());// 상품 찾고
		List<OrderItem> orderItemList = new ArrayList<>();

		OrderItem orderItem = OrderItem.builder().qty(ordersProductDto.getOrderItem_qty()).optionSet(optionSet).build();// orderItem찾음

		orderItemList.add(orderItem);// 상품에서 직접 주문하는거니까 orderItem하나만 들어간다

		Delivery delivery = Delivery.builder().name(ordersProductDto.getDelivaryName())
				.phoneNumber(ordersProductDto.getDelivaryPhoneNumber()).address(ordersProductDto.getDelivaryAddress())
				.detailAddress(ordersProductDto.getDelivaryDetailAddress())
				.postCode(ordersProductDto.getDeliveryPostCode()).build();

		System.out.println("@@@@@@@@@@@@@@@delivery= " + delivery);

		Orders orders = orderDao.save(Orders.builder().description(optionSet.getProduct().getName())
				.price(optionSet.getProduct().getPrice() * ordersProductDto.getOrderItem_qty())
				.statement(OrderStateMsg.입금대기중).member(Member.toResponseEntity(memberService.getMemberBy(sUserId)))
				.delivery(delivery).orderItems(orderItemList).build());

		delivery.setOrders(orders);
		orderItem.setOrders(orders); // transactional

		MemberResponseDto memberResponseDto = memberService.getMemberBy(sUserId);

		Member member1 = Member.toResponseEntity(memberResponseDto);

		memberService.updateGrade(member1, (int) ((orders.getPrice()) * 0.001));

		orders.setMember(Member.toResponseEntity(memberResponseDto));

		return OrdersDto.orderDto(orders);

	}
	/*
	 * cart에서 주문(회원)
	 */
//	   @Transactional
//	   public OrdersDto memberCartOrderSave(String sUserId,DeliveryDto deliveryDto) throws Exception {
//	      
//	      
//	      List<SUserCartResponseDto> cartList= cartService.findsUserCartList(sUserId);
//	      
//	      int o_tot_price = 0;
//	      int oi_tot_count = 0;
//	      List<OrderItem> orderItemList = new ArrayList<>();
//	      for (SUserCartResponseDto cart : cartList) {
//	    	  OptionSet optionSet = optionSetDao.findById(cart.getOsId());
//	         o_tot_price+=optionSet.getTotalPrice()*(cart.getQty());
//	         oi_tot_count+=cart.getQty();
//	      
//	         
//	         OrderItem inputOIEntity = OrderItem.builder()
//		               .qty(cart.getQty())
//		               .optionSet(optionSet)
//		               .build();
//	         
//	         orderItemList.add(inputOIEntity);
//	      }
//	      
//	       OptionSet optionSet= optionSetDao.findById(orderItemList.get(0).getOptionSet().getId());
//	      
//	       String o_desc = optionSet.getProduct().getName() + "외" + (oi_tot_count - 1) + "개";
//	       if (oi_tot_count == 1) {
//	    	   o_desc = optionSet.getProduct().getName();
//	       }
//		   	Delivery delivery = Delivery.builder()
//										.name(deliveryDto.getName())
//										.phoneNumber(deliveryDto.getPhoneNumber())
//										.address(deliveryDto.getAddress())
//										.build();
//						      
//	       
//	      Orders orders = orderDao.save(Orders.builder()
//	                            .description(o_desc)
//	                            .price(o_tot_price)
//	                            .statement(OrderStateMsg.입금대기중)
//	                            .member(Member.toResponseEntity(memberService.getMemberBy(sUserId)))
//	                            .delivery(delivery)
//	                            .orderItems(orderItemList)
//	                            .build());
//	      
//	      for (OrderItem orderItem : orderItemList) {
//			orderItem.setOrders(orders);
//		}
//	      delivery.setOrders(orders);
//	      
//	      
//		    MemberResponseDto memberResponseDto =memberService.getMemberBy(sUserId);
//			
//		      Member member1= Member.toResponseEntity(memberResponseDto);
//		      
//		       memberService.updateGrade(member1,(int)((orders.getPrice())*0.001));
//		     
//		      orders.setMember(Member.toResponseEntity(memberResponseDto));
////		      cartService.deleteCarts(sUserId);
//	      return OrdersDto.orderDto(orders);
//	   }

	/*
	 * cart에서 선택주문(회원)
	 */
	@Transactional
	public OrdersDto memberCartSelectOrderSave(String sUserId, DeliveryDto deliveryDto, List<CartDto> fUserCarts)
			throws Exception {

		ArrayList<OrderItem> orderItemList = new ArrayList<>();

		int o_tot_price = 0;
		int oi_tot_count = 0;

		MemberResponseDto memberResponseDto = memberService.getMemberBy(sUserId);

		Member member = Member.toResponseEntity(memberResponseDto);

		for (int i = 0; i < fUserCarts.size(); i++) {

			CartDto cartDto = fUserCarts.get(i);
			OptionSet optionSet = optionSetDao.findById(cartDto.getOptionSetId());

			o_tot_price += (optionSet.getTotalPrice()) * (cartDto.getQty());
			oi_tot_count += cartDto.getQty();

			OrderItem inputOIEntity = OrderItem.builder().qty(cartDto.getQty()).optionSet(optionSet).build();

			orderItemList.add(inputOIEntity);
		}

		OptionSet optionSet = optionSetDao.findById(orderItemList.get(0).getOptionSet().getId());

		String o_desc = optionSet.getProduct().getName() + "외" + (oi_tot_count - 1) + "개";

		if (oi_tot_count == 1) {
			o_desc = optionSet.getProduct().getName();
		}

		Delivery delivery = Delivery.builder().name(deliveryDto.getName()).phoneNumber(deliveryDto.getPhoneNumber())
				.address(deliveryDto.getAddress()).detailAddress(deliveryDto.getDetailAddress())
				.postCode(deliveryDto.getPostCode()).build();
		System.out.println("@@@@@@@@@@@@@@@@@@delivery: " + delivery);

		Orders orders = Orders.builder().description(o_desc).price(o_tot_price).statement(OrderStateMsg.입금대기중)
				.member(member).delivery(delivery).orderItems(orderItemList).build();
		System.out.println("@@@@@@@@@@@@@@@@@@orders: " + orders);

		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrders(orders);
		}
		delivery.setOrders(orders);
		orders.setMember(member);
		Orders realOrders = orderDao.save(orders);// 마지막에 세이브해야되는듯
		System.out.println("@@@@@@@@@@@@@@@@@@delivery: " + delivery);
		System.out.println("@@@@@@@@@@@@@@@@@@orderItemList: " + orderItemList);
		System.out.println("@@@@@@@@@@@@@@@@@@real orders: " + realOrders);

		memberService.updateGrade(member, (int) ((orders.getPrice()) * 0.001));

		orders.setMember(Member.toResponseEntity(memberResponseDto));

		System.out.println("5555555555555555555555555555555" + member);
		return OrdersDto.orderDto(orders);
	}

	/*
	 * 주문+주문아이템 목록(회원)
	 */

	@Transactional
	public List<OrdersDto> memberOrderList(String value) throws Exception {
		List<OrdersDto> ordersDtoList = new ArrayList<>();
		if (value == null) {
			throw new Exception("일치하는 사용자가없습니다.");
		} 
		if(value.contains("@")) {
			List<Orders> orderList = orderDao.findOrdersByMember_Email(value);
			
			for (Orders orders : orderList) {
				OrdersDto ordersDto = OrdersDto.orderDto(orders);
				ordersDtoList.add(ordersDto);
			}
		}else {
		List<Orders> orderList = orderDao.findOrdersByMember_UserName(value);
		for (Orders orders : orderList) {
			OrdersDto ordersDto = OrdersDto.orderDto(orders);
			ordersDtoList.add(ordersDto);
		}
		}
		return ordersDtoList;
	}

	/*
	 * 주문상세보기(회원)
	 */
	@Transactional
	public OrdersDto memberOrderDetail(Long orderNo) throws Exception {

		Orders orders = orderDao.findById(orderNo);

		return OrdersDto.orderDto(orders);
	}

	/******************************* 공용 ****************************/

	/*
	 * 1.정상주문
	 */
	@Transactional
	@Override
	public OrdersDto updateStatementByNormalOrder(Long orderNo) throws Exception {

		Orders updateOrder = orderDao.updateStatementByNormalOrder(orderNo);
		orderDao.save(updateOrder);
		return OrdersDto.orderDto(updateOrder);
	}

	/*
	 * 2.취소주문
	 */
	@Transactional
	@Override
	public OrdersDto updateStatementByCancleOrder(Long orderNo) throws Exception {
		Orders updateOrder = orderDao.updateStatementByCancleOrder(orderNo);
		orderDao.save(updateOrder);
		return OrdersDto.orderDto(updateOrder);
	}

	/*
	 * 3.환불주문(client)
	 */
	@Transactional
	@Override
	public OrdersDto updateStatementByClientRefundOrder(Long orderNo) throws Exception {
		Orders updateOrder = orderDao.updateStatementByClientRefundOrder(orderNo);
		orderDao.save(updateOrder);
		return OrdersDto.orderDto(updateOrder);
	}

	/*
	 * 4.환불주문(admin)
	 */
	@Transactional
	@Override
	public OrdersDto updateStatementByAdminRefundOrder(Long orderNo) throws Exception {
		Orders updateOrder = orderDao.updateStatementByAdminRefundOrder(orderNo);
		orderDao.save(updateOrder);
		return OrdersDto.orderDto(updateOrder);
	}

	/*
	 * 5.상태리셋
	 */
	@Transactional
	@Override
	public OrdersDto updateStatementByResetOrder(Long orderNo) throws Exception {
		Orders updateOrder = orderDao.updateStatementByResetOrder(orderNo);
		orderDao.save(updateOrder);
		return OrdersDto.orderDto(updateOrder);
	}
	
	
}
