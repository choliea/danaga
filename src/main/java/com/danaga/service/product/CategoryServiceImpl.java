package com.danaga.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danaga.dao.product.CategoryDao;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.CategoryDto;
import com.danaga.entity.Category;
import com.danaga.exception.product.FoundNoObjectException.FoundNoCategoryException;
import com.danaga.exception.product.ProductSuccessMsg;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
	
	private final CategoryDao categoryDao;
	
	//최상위 카테고리만 조회
	@Override
	public ResponseDto<CategoryDto> AncestorCategories() {
		List<CategoryDto> data = categoryDao.findByParentEmpty().stream().map(t->new CategoryDto(t)).collect(Collectors.toList());
		return ResponseDto.<CategoryDto>builder().data(data).msg(ProductSuccessMsg.TOP_CATEGORY).build();
	}
	//카테고리의  자식들 조회
	@Override
	public ResponseDto<?> categoryFamily(Long id) {
		List<CategoryDto> data = categoryDao.findChildTypesByParentId(id).stream().map(t->new CategoryDto(t)).collect(Collectors.toList());
		return ResponseDto.<CategoryDto>builder().data(data).msg(ProductSuccessMsg.CHILDREN_CATEGORY).build();
	}

	//카테고리 생성
	@Override
	public ResponseDto<?> create(@Valid CategoryDto.CategorySaveDto dto) {
		List<CategoryDto> data = new ArrayList<>();
		data.add(categoryDao.create(dto));
		return ResponseDto.<CategoryDto>builder().data(data).msg(ProductSuccessMsg.ADD_CATEGORY).build();
	}
	//카테고리 수정
	@Override
	@Transactional
	public ResponseDto<?> update(@Valid CategoryDto dto) {
		List<CategoryDto> data = new ArrayList<>();
		try {
			CategoryDto updateDto = categoryDao.update(dto);
			data.add(updateDto); 
		} catch (FoundNoCategoryException e) {
			e.printStackTrace();
			return ResponseDto.builder().msg(e.getMsg()).build();
		}
		return ResponseDto.<CategoryDto>builder().data(data).msg(ProductSuccessMsg.UPDATE_CATEGORY).build();
	}
	//카테고리 삭제
	@Override
	@Transactional
	public ResponseDto<?> delete(Long id) {
		try {
			categoryDao.delete(id);
		} catch (FoundNoCategoryException e) {
			e.printStackTrace();
			return ResponseDto.<CategoryDto>builder().msg(e.getMsg()).build();
		}
		return ResponseDto.builder().msg(ProductSuccessMsg.REMOVE_CATEGORY).build();
	}
	//최하위 카테고리인지 
	@Override
	public Boolean isYoungest(Long id){
		List<Category> children = categoryDao.findChildTypesByParentId(id);
		return children.size()==0 ? true : false;
	}

}
