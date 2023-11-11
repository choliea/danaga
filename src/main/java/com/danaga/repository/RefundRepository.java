package com.danaga.repository;

import org.springframework.data.jpa.repository.*;

import com.danaga.entity.*;

public interface RefundRepository extends JpaRepository<Refund,Long> {
	//오더pk로 환불객체 하나 찾기
	Refund findRefundByOrdersId(Long id);
	
		

}
