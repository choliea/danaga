package com.danaga.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import jakarta.transaction.Transactional;

@SpringBootTest
class OrderDaoImplTest {

	@Autowired
	OrderDao orderDao;
	
	@Test
	@Transactional
	@Rollback(false)
	void testOrdersMemberIdNull()throws Exception {
		orderDao.ordersMemberIdNull("Kakao3152284904");
	}

}
