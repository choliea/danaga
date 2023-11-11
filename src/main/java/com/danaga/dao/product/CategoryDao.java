package com.danaga.dao.product;

import java.util.List;

import com.danaga.dto.product.CategoryDto;
import com.danaga.entity.Category;
import com.danaga.exception.product.FoundNoObjectException.FoundNoCategoryException;

public interface CategoryDao {

	List<Category> findChildTypesByParentId(Long id);

	CategoryDto update(CategoryDto dto) throws FoundNoCategoryException;

	void delete(Long id) throws FoundNoCategoryException;

	Category findById(Long id) throws FoundNoCategoryException;
	
	List<Category> findByParentEmpty();
	
	List<Category> findByOptionSetId(Long optionSetId);

	CategoryDto create(CategoryDto.CategorySaveDto dto);

}
