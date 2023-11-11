package com.danaga.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danaga.entity.CategorySet;

public interface CategorySetRepository extends JpaRepository<CategorySet, Long>{
	Integer countByCategoryId(Long categoryId); 
}
