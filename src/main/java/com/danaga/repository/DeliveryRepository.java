package com.danaga.repository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.jpa.repository.*;

import com.danaga.dao.*;
import com.danaga.dto.*;
import com.danaga.entity.*;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	
	// 주문번호로 배송내역찾기
	Delivery findDeliveryByOrdersId(Long id);// 주문목록에서 현재 주문상태가 배송중, 배송완료일 때 배송번호나오게하기

	

	
	

}
