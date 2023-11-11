package com.danaga.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StatisticRepositoryTest {

	@Autowired
	StatisticRepository st;
	
	@Test
	@Disabled
	void test() {
		System.out.println(st.findByIdStartsWith("202310"));
	}

}
