package com.danaga.service.product;

import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.OptionDto;
import com.danaga.dto.product.OptionSetUpdateDto;
import com.danaga.dto.product.OtherOptionSetDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.dto.product.ProductSaveDto;
import com.danaga.dto.product.ProductUpdateDto;
import com.danaga.dto.product.QueryStringDataDto;
import com.danaga.dto.product.UploadProductDto;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionsException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoProductException;

public interface OptionSetService {

		ResponseDto<?> deleteProduct(Long productId) throws FoundNoProductException;
		ResponseDto<?> deleteOptionSet(Long optionSetId) throws FoundNoOptionSetException;
		ResponseDto<?> deleteOption(Long optionId) throws FoundNoOptionsException;
 		
		ResponseDto<?> updateStock(OptionSetUpdateDto dto) throws FoundNoOptionSetException;
		ResponseDto<?> updateOrderCount(Long optionSetId, Integer orderCount) throws FoundNoOptionSetException;
		ResponseDto<?> updateViewCount(Long optionSetId) throws FoundNoOptionSetException;
		ResponseDto<?> update(OptionDto dto) throws FoundNoOptionsException;

		ResponseDto<?> uploadProduct(UploadProductDto dto);
		
		ResponseDto<ProductDto> findById(Long optionSetId);
		ResponseDto<?> showOptionNameValues(Long categoryId);
		
		ResponseDto<ProductListOutputDto> displayHitProducts(Long optionSetId,Integer firstResult);
		
		ResponseDto<ProductListOutputDto> searchProducts(QueryStringDataDto dto,Integer firstResult);
		
		ResponseDto<OtherOptionSetDto> showOtherOptionSets(Long optionSetId);
		ResponseDto<?> showAllOptionNameValues(Long categoryId);
		ResponseDto<ProductListOutputDto> searchProductsForMember(QueryStringDataDto dto, String username,Integer firstResult);
		ResponseDto<ProductListOutputDto> displayHitProductsForMember(Long optionSetId, String username,Integer firstResult);
		
}
