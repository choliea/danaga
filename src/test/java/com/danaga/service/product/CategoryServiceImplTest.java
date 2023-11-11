package com.danaga.service.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.danaga.dto.product.CategoryDto;
import com.danaga.dto.product.CategoryDto.CategorySaveDto;

import jakarta.transaction.Transactional;
@SpringBootTest
class CategoryServiceImplTest {
@Autowired
private CategoryService service;
	@Test
	@Disabled
	void testAncestorCategories() {
		System.out.println(service.AncestorCategories());
	}

	@Test
//	@Disabled
	void testCategoryFamily() {
		System.out.println(service.categoryFamily(1L));
	}

	@Test
	@Disabled
	void testCreate() {
		service.create(CategorySaveDto.builder()
				.name("custom")
				.parentId(3L)
				.build());
	}

	@Test
	@Disabled
	void testUpdate() {
		service.update(CategoryDto.builder()
				.id(52L)
				.name("customUpdate")
				.parentId(4L)
				.build());
	}

	@Test
	@Transactional
	@Rollback(false)
	@Disabled
	void testDelete() {
		service.delete(10L);
	}

	@Test
	@Disabled
	void testIsYoungest() {
		System.out.println(service.isYoungest(1L));
	}

}
