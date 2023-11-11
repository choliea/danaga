package com.danaga.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.danaga.entity.Cart;



public interface CartRepository extends JpaRepository<Cart, Long> {

	// 유저 카트리스트
	List<Cart> findByMemberId(Long memberId);

	// 유저 카트 제품한개
	Cart findByOptionSetIdAndMemberId(Long optionSetId, Long memberId);

	// 장바구니 수량
	int countByMemberId(Long memberId);
	
	void deleteByMemberId(Long memberId);

}
