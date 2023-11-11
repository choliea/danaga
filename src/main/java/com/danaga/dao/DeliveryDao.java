package com.danaga.dao;

import com.danaga.entity.*;

public interface DeliveryDao {
	//배송신청
	Delivery insertDelivery(Delivery delivery, Long orderId);// 배송정보 입력후 배송하기 눌렀을 때
	
	//update는 없음
	
	//배송취소                           Orders의 oState가 취소로 변경된다.
//	void deleteDelivery(Long id); // 주문상태가 입금대기중인 상태에서  배송취소가 가능하기 때문에
									// 주문신청을 했을 떄 주문정보와 배송정보를 입력했기 때문에 
									//배송도 취소하고 oState상태도 취소로 만들어야함(결제안해서 관리자권한필요없어)
}


/*
   public static final String msg1 = "입금대기중";
   public static final String msg2 = "배송중";
   public static final String msg3 = "배송완료";
   public static final String msg4 = "환불대기중";
   public static final String msg5 = "환불완료";
   public static final String msg6 = "취소";
 */