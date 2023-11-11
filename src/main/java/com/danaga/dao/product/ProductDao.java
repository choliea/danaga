package com.danaga.dao.product;

import com.danaga.dto.product.ProductSaveDto;
import com.danaga.entity.Product;
import com.danaga.exception.product.FoundNoObjectException.FoundNoProductException;

public interface ProductDao {

	Product findById(Long id) throws FoundNoProductException;
	Product findByOptionSetId(Long optionSetId) throws FoundNoProductException;
	Product create(ProductSaveDto dto);
	void deleteById(Long id) throws FoundNoProductException;
}
