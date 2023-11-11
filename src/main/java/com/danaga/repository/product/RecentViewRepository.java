package com.danaga.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danaga.entity.RecentView;

public interface RecentViewRepository extends JpaRepository<RecentView, Long>{

	void deleteByMemberIdAndOptionSetId(Long memberId, Long optionSetId);
	
	void deleteByMemberId(Long memberId);
	
	RecentView findByMemberIdAndOptionSetId(Long memberId,Long optionSetId);

	List<RecentView> findByMemberId(Long memberId);
	
}
