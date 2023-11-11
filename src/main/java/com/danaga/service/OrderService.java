package com.danaga.service;

import java.util.List;

import com.danaga.dao.OrderDao;
import com.danaga.entity.Delivery;

import com.danaga.dto.*;

import com.danaga.entity.Member;
import com.danaga.entity.Orders;

public interface OrderService {
	/*****************************비회원*************************/
	
	/*
	 * 카트에서 주문(비회원)
	 */
//	OrdersDto guestCartOrderSave(List<CartDto> fUserCarts, DeliveryDto deliveryDto, OrderGuestDto orderGuestDto)  throws Exception; 
	/*
	 * 상품에서 직접주문(비회원)
	 */
	OrdersDto guestProductOrderSave(OrdersProductDto ordersProductDto,OrderGuestDto orderGuestDto) throws Exception;
	/*
	 * cart에서 선택주문(비회원)
	 */
	OrdersDto guestCartSelectOrderSave(DeliveryDto deliveryDto,List<CartDto> fUserCarts,OrderGuestDto orderGuestDto)throws Exception;
	/*
	 * 주문+주문아이템 목록(비회원)
	 */
	List<OrdersDto> guestOrderList(Long orderNo, String phoneNumber)throws Exception;
	/*
	 * 주문 디테일(비회원)
	 */
	OrdersDto guestOrderDetail(Long orderNo, String phoneNo)throws Exception;
	
	
	/*****************************회원**************************/
	
	/*
	 * 상품에서 직접주문(회원)
	 */
	OrdersDto memberProductOrderSave(String sUserId,OrdersProductDto ordersProductDto) throws Exception;

	/*
	 * cart에서 주문(회원)
	 */
//	OrdersDto memberCartOrderSave(String sUserId,DeliveryDto deliveryDto) throws Exception;

	/*
	 * cart에서 선택주문(회원)
	 */
	OrdersDto memberCartSelectOrderSave(String sUserId,DeliveryDto deliveryDto,List<CartDto> optionSetIdArray)throws Exception;
	/*
	 * 주문+주문아이템 목록(회원)
	 */
	List<OrdersDto> memberOrderList(String userName)throws Exception;
	/*
	 * 주문상세보기(회원)
	 */
	OrdersDto memberOrderDetail(Long orderNo) throws Exception;
	
	/**********************공용*******************/
	// 주문상태업데이트(특정주문)
	// 1.정상주문
	OrdersDto updateStatementByNormalOrder(Long orderNo)throws Exception;

	// 2.취소주문
	OrdersDto updateStatementByCancleOrder(Long orderNo)throws Exception;

	// 3.환불주문
	OrdersDto updateStatementByClientRefundOrder(Long orderNo)throws Exception;
	// 4.환불주문
	OrdersDto updateStatementByAdminRefundOrder(Long orderNo)throws Exception;
	// 5.상태리셋
	OrdersDto updateStatementByResetOrder(Long orderNo)throws Exception;
	

}
