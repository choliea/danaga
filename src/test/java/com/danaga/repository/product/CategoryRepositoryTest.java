package com.danaga.repository.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.danaga.repository.product.CategoryRepository;
@SpringBootTest
class CategoryRepositoryTest {
	@Autowired
	private CategoryRepository repository;
	@Test
	void test() {
		System.out.println(repository.findChildTypesByParent_Id(1L));
	}

}
