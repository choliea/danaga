package com.danaga.dao.product;

import java.util.List;

import com.danaga.dto.product.OptionDto;
import com.danaga.entity.Options;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionsException;
import com.danaga.repository.product.OptionNamesValues;

public interface OptionsDao {
	
	Options findById(Long id) throws FoundNoOptionsException;
	
	List<Options> findOptionsByOptionSetId(Long optionSetId);
	
	void deleteAllByOptionSetId(Long optionSetId) throws FoundNoOptionsException;
	
	void deleteById(Long id) throws FoundNoOptionsException;
	Options update(OptionDto dto) throws FoundNoOptionsException;
	List<OptionNamesValues> findOptionNameValueMapByCategoryId(Long id);

	Options save(OptionDto.OptionSaveDto dto);
	
	
}
