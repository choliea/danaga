package com.danaga.repository.product;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.danaga.dto.product.CategoryDto;
import com.danaga.dto.product.QueryStringDataDto;
@SpringBootTest
class OptionSetQueryRepositoryTest {

	@Autowired
	OptionSetQueryRepository repository;
	@Test
	void test() {
		System.out.println("Aaaaaaaaaaaaaaaaaaaa"+repository.findForMemberByFilter(
				QueryStringDataDto.builder()
				.orderType(OptionSetQueryData.BY_TOTAL_PRICE)
				.build(),"User5").size());
		List<com.danaga.dto.product.OptionDto.OptionNameValueMapDto> list = new ArrayList<>();
		List<String> values = new ArrayList<>();
		values.add("windows11");
		values.add("windows10");
		
		list.add(com.danaga.dto.product.OptionDto.OptionNameValueMapDto.builder().optionName("os").optionValue(values).build());
		System.out.println(repository.findByFilter(
				QueryStringDataDto.builder()
				.orderType(OptionSetQueryData.BY_TOTAL_PRICE)
				.category(CategoryDto.builder().name("전체").parentId(1L)
						.build())
				.minPrice(0)
				.nameKeyword("Mr")
				.optionset(list)
				.build()));
	}

}
