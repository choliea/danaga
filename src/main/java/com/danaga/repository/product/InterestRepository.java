package com.danaga.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danaga.entity.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {

	
	//상품페이지에서 유저가 관심상품 체크했는지 표시하려면 
	Boolean existsByMemberIdAndOptionSetId(Long memberId,Long optionSetId);
	
	void deleteByOptionSetIdAndMemberId(Long optionSetId, Long memberId);
	
	void deleteByMemberId(Long memberId);
}
