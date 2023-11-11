package com.danaga.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danaga.entity.Options;

public interface OptionsRepository extends JpaRepository<Options, Long> {
	List<OptionNamesValues> findDistinctByOptionSet_Product_CategorySets_Category_Id(Long id);
	
	List<Options> findAllByOptionSetId(Long optionSetId);
	
	void deleteAllByOptionSetId(Long optionSetId);
}
