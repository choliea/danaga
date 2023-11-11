package com.danaga.dao.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.danaga.dto.product.CategoryDto;
import com.danaga.entity.Category;
import com.danaga.exception.product.FoundNoObjectException.FoundNoCategoryException;
import com.danaga.repository.product.CategoryRepository;
@Repository
public class CategoryDaoImpl implements CategoryDao{
	
	@Autowired
	private CategoryRepository repository;
	@Override
	public List<Category> findChildTypesByParentId(Long id){
		return repository.findChildTypesByParent_Id(id);
	}
	@Override
	public CategoryDto create(CategoryDto.CategorySaveDto dto) {
		Category entity = dto.toEntity();
		Category saved = repository.save(entity);
		return new CategoryDto(repository.findById(saved.getId()).get());
	}
	
	public CategoryDto update(CategoryDto dto) throws FoundNoCategoryException {
		Category find= repository.findById(dto.getId()).orElseThrow(() -> new FoundNoCategoryException());
		find.setName(dto.getName());
		find.setParent(Category.builder().id(dto.getParentId()).build());
		Category updated = repository.save(find);
		return new CategoryDto(repository.findById(updated.getId()).get());
	}
	
	@Override
	public void delete(Long id) throws FoundNoCategoryException {
		Category find= repository.findById(id).orElseThrow(() -> new FoundNoCategoryException());
		repository.deleteById(find.getId());
	}
	@Override
	public Category findById(Long id) throws FoundNoCategoryException {
		return repository.findById(id).orElseThrow(() -> new FoundNoCategoryException());
	}
	@Override
	public List<Category> findByParentEmpty() {
		return repository.findByParentNull();
	}
	@Override
	public List<Category> findByOptionSetId(Long optionSetId) {
		return repository.findByCategorySets_Product_OptionSets_Id(optionSetId);
	}
}
