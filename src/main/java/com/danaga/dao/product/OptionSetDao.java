package com.danaga.dao.product;

import java.util.List;

import com.danaga.dto.product.OptionSetCreateDto;
import com.danaga.dto.product.OptionSetUpdateDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.dto.product.QueryStringDataDto;
import com.danaga.entity.OptionSet;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionsException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoProductException;

public interface OptionSetDao {

	OptionSet findById(Long id) throws FoundNoOptionSetException;
	List<OptionSet> findAllByProductId(Long productId);
	OptionSet create(OptionSetCreateDto dto);
	void deleteById(Long id) throws FoundNoOptionSetException;
	void deleteAllByProductId(Long productId) throws FoundNoProductException;
	OptionSet findByOptionId(Long optionId) throws FoundNoOptionsException;
	List<OptionSet> findAllByRecentView_MemberId(Long memberId) throws FoundNoMemberException;
	List<OptionSet> findAllByInterest_MemberId(Long memberId) throws FoundNoMemberException;
	List<ProductListOutputDto> findByFilter(QueryStringDataDto dataDto,Integer firstResult);
	OptionSet updateStock(OptionSetUpdateDto dto) throws FoundNoOptionSetException;
	OptionSet updateOrderCount(Long id,Integer orderCount) throws FoundNoOptionSetException;
	OptionSet updateViewCount(Long id) throws FoundNoOptionSetException;
	OptionSet calculateTotalPrice(OptionSetUpdateDto dto) throws FoundNoOptionSetException;
	List<ProductListOutputDto> findForMemberByFilter(QueryStringDataDto dataDto, String username,Integer firstResult) throws FoundNoMemberException;
	
	
}
