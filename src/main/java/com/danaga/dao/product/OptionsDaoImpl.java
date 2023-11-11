package com.danaga.dao.product;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.danaga.dto.product.OptionDto;
import com.danaga.entity.Options;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionsException;
import com.danaga.repository.product.OptionNamesValues;
import com.danaga.repository.product.OptionsRepository;

import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor
public class OptionsDaoImpl implements OptionsDao{

	private final OptionsRepository optionsRepository;
	@Override
	public List<OptionNamesValues> findOptionNameValueMapByCategoryId(Long id) {
		return optionsRepository.findDistinctByOptionSet_Product_CategorySets_Category_Id(id);
	}

	@Override
	public Options findById(Long id) throws FoundNoOptionsException {
		return optionsRepository.findById(id).orElseThrow(FoundNoOptionsException::new);
	}

	@Override
	public List<Options> findOptionsByOptionSetId(Long optionSetId) {
		return optionsRepository.findAllByOptionSetId(optionSetId);
	}
	
	@Override
	public Options save(OptionDto.OptionSaveDto dto) {
		return optionsRepository.save(dto.toEntity());
	}

	@Override
	public void deleteAllByOptionSetId(Long optionSetId) throws FoundNoOptionsException {
		List<Options> findOptions=optionsRepository.findAllByOptionSetId(optionSetId);
		if(findOptions==null) {
			throw new FoundNoOptionsException();
		}
		optionsRepository.deleteAllByOptionSetId(optionSetId);
	}

	@Override
	public void deleteById(Long id) throws FoundNoOptionsException {
		optionsRepository.findById(id).orElseThrow(FoundNoOptionsException::new);
		optionsRepository.deleteById(id);
	}
	@Override
	public Options update(OptionDto dto) throws FoundNoOptionsException {
		Options origin = optionsRepository.findById(dto.getId()).orElseThrow(FoundNoOptionsException::new);
		origin.setExtraPrice(dto.getExtraPrice());
		origin.setName(dto.getName());
		origin.setValue(dto.getValue());
		return optionsRepository.save(origin);
	}

}
