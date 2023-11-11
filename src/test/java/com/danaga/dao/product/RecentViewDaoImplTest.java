package com.danaga.dao.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.danaga.exception.product.FoundNoObjectException;

import jakarta.transaction.Transactional;
@SpringBootTest
class RecentViewDaoImplTest {

	@Autowired
	private RecentViewDao dao;
	@Test
	@Transactional
	@Rollback(false)
	void test() throws FoundNoObjectException {
		dao.deleteAll(14L);
	}

}
