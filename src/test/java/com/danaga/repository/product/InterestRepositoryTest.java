package com.danaga.repository.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.danaga.dto.product.InterestDto;
import com.danaga.repository.product.InterestRepository;

import jakarta.transaction.Transactional;
@SpringBootTest
class InterestRepositoryTest {

	@Autowired
	private InterestRepository repository;
	@Test
	@Transactional
	@Rollback(false)
	void test() {
		//System.out.println(repository.existsByMemberIdAndOptionSetId(2L, 4L));
		repository.deleteByMemberId(1L);
		repository.deleteByOptionSetIdAndMemberId(6L,3L);
	}

}
