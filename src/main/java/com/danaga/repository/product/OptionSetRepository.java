package com.danaga.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danaga.entity.OptionSet;

public interface OptionSetRepository extends JpaRepository<OptionSet, Long>{
	
	List<OptionSet> findByProductId(Long id);
	
	void deleteAllByProductId(Long productId);
	OptionSet findByOptions_Id(Long optionId);
	List<OptionSet> findAllByRecentViews_MemberId(Long memberId);
	List<OptionSet> findAllByInterests_MemberId(Long memberId);
}
