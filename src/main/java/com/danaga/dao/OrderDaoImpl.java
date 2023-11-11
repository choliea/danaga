package com.danaga.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Repository;

import com.danaga.config.OrderStateMsg;
import com.danaga.dto.OrderItemDto;
import com.danaga.entity.Delivery;
import com.danaga.entity.Member;
import com.danaga.entity.OptionSet;
import com.danaga.entity.OrderItem;
import com.danaga.entity.Orders;
import com.danaga.repository.DeliveryRepository;
import com.danaga.repository.MemberRepository;
import com.danaga.repository.OrderItemRepository;
import com.danaga.repository.OrderRepository;

import jakarta.persistence.criteria.Order;
import jakarta.transaction.Transactional;

@Repository
public class OrderDaoImpl implements OrderDao {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	// 주문SAVE
	public Orders save(Orders orders) throws Exception {

		return orderRepository.save(orders);
	}

	// 주문상태업데이트
	// 1.정상주문
	@Override
	public Orders updateStatementByNormalOrder(Long orderNo) throws Exception {

		if (orderNo != null) {
			Orders findOrder = orderRepository.findById(orderNo).get();

			if (findOrder.getStatement() == OrderStateMsg.입금대기중) {
				findOrder.setStatement(OrderStateMsg.배송중);
			} else if (findOrder.getStatement() == OrderStateMsg.배송중) {
				findOrder.setStatement(OrderStateMsg.배송완료);
			}
			return findOrder;
		} else {//주문번호가 null이면
			throw new Exception("일치하는 주문번호가 없습니다.");
		}
	}

	// 2.취소주문
	public Orders updateStatementByCancleOrder(Long orderNo) throws Exception {
		
		if (orderNo != null) {
		Orders findOrder = orderRepository.findById(orderNo).get();

		if (findOrder.getStatement() == OrderStateMsg.입금대기중) {
			findOrder.setStatement(OrderStateMsg.취소);
		}
		return findOrder;
		} else {
			throw new Exception("일치하는 주문번호가 없습니다.");
		}
		
	}

	// 3.환불주문(client)
	public Orders updateStatementByClientRefundOrder(Long orderNo) throws Exception {
		if (orderNo != null) {
		Orders findOrder = orderRepository.findById(orderNo).get();
		if (findOrder.getStatement() == OrderStateMsg.배송완료) {
			findOrder.setStatement(OrderStateMsg.환불대기중);
		}

		return findOrder;
		} else {
			throw new Exception("일치하는 주문번호가 없습니다.");
		}
	}

	// 4.환불주문(admin)
	public Orders updateStatementByAdminRefundOrder(Long orderNo) throws Exception {
		if (orderNo != null) {
		Orders findOrder = orderRepository.findById(orderNo).get();
		if (findOrder.getStatement() == OrderStateMsg.환불대기중) {
			findOrder.setStatement(OrderStateMsg.환불완료);
		}

		return findOrder;
		} else {
			throw new Exception("일치하는 주문번호가 없습니다.");
		}
	}

	// 5.상태리셋
	public Orders updateStatementByResetOrder(Long orderNo) throws Exception {
		if (orderNo != null) {
		Orders findOrder = orderRepository.findById(orderNo).get();
		findOrder.setStatement(OrderStateMsg.입금대기중);
		return findOrder;
		} else {
			throw new Exception("일치하는 주문번호가 없습니다.");
		}
	}

	// Id로 주문전체(특정사용자)

	@Override
	public List<Orders> findOrdersByMember_UserName(String userName) throws Exception {
		if(userName!=null) {
			List<Orders> orders = orderRepository.findOrdersByMember_UserName(userName);
			return orders;
			
		} else {
			throw new Exception("일치하는 이름이 없습니다.");
		}
	}
	
	// Email로 주문전체(특정사용자)

	@Override
	public List<Orders> findOrdersByMember_Email(String email) throws Exception {
		if(email!=null) {
			List<Orders> orders = orderRepository.findOrdersByMember_Email(email);
			return orders;
			
		} else {
			throw new Exception("일치하는 이메일이 없습니다.");
		}
	}
	

	// 주문 번호로 1개보기(주문상세리스트)
	// 주문번호는 id+10000이므로 받을때 id-10000으로 받아야함 --컨트롤러에서!!
	@Override
	public Orders findById(Long id) throws Exception {
		Orders ordersDetail = orderRepository.findById(id).get();
		if (ordersDetail != null) {
			return ordersDetail;
		} else {
			throw new Exception("일치하는 주문번호가 없습니다.");
		}
	}

	// 비회원(주문번호,회원이름,회원전화번호) 로 주문1개보기
	@Override
	public Orders findOrdersByIdAndPhoneNo(Long orderNo, String phoneNo) throws Exception {
		if((orderNo != null)&&(phoneNo != null)) {
		Orders guestFindOrder = orderRepository.findOrdersByIdAndMember_PhoneNo(orderNo,
				phoneNo);
		return guestFindOrder;
		} else if(orderNo == null) {
			throw new Exception("주문번호가 없습니다.");
		}  else {
			throw new Exception("전화번호가 없습니다.");
		}
		
	}
	/*
	 * 주문+주문아이템 목록(회원)
	 */

	@Transactional
	public List<Orders> memberOrdersListNull(String userName) throws Exception {

		if (userName == null) {
			throw new Exception("일치하는 사용자가없습니다.");
		}
		List<Orders> orderList = this.findOrdersByMember_UserName(userName);
		System.out.println("3333333333333333"+orderList);
		
		return orderList;
	}
	/*
	 * 오더에 있는 멤버 삭제 (회원탈퇴시 필요)
	 */
	@Transactional
	public void ordersMemberIdNull(String userName) throws Exception {

		List<Orders> ordersList = this.memberOrdersListNull(userName);
		
		for (int i = 0; i < ordersList.size(); i++) {
			
			ordersList.get(i).setMember(null);
			System.out.println("&&&&&&&&&&&&&"+ordersList.get(i)); 
			System.out.println("###################"+ordersList.get(i));
			orderRepository.save(ordersList.get(i));
		}
	}

}
