package com.danaga.repository.product;

import static org.assertj.core.api.Assertions.filter;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.danaga.entity.Options;
import com.danaga.repository.product.OptionNamesValues;
import com.danaga.repository.product.OptionsRepository;

@SpringBootTest
class OptionsRepositoryTest {

	@Autowired
	private OptionsRepository repository;
	
	@Test
	void test() {
		System.out.println("ㅁaaaaa");
//		System.out.println(repository.findDistinctNameByOptionSet_Product_CategorySets_Category_Id(4L).stream().map(name->name.getName()).collect(Collectors.toList()));
//		System.out.println(repository.findDistinctValueByOptionSet_Product_CategorySets_Category_Id(4L).stream().map(value->value.getValue()).collect(Collectors.toList()));
		List<OptionNamesValues> o = repository.findDistinctByOptionSet_Product_CategorySets_Category_Id(4L);
//		Map<String, Set<String>> map = new HashMap<>();
//		o.forEach(option -> map.computeIfAbsent(option.getName(), k -> new HashSet<>()).add(option.getValue()));
		Map<String, Set<String>> map = o.stream().collect(Collectors.groupingBy(OptionNamesValues::getName, 
                Collectors.mapping(OptionNamesValues::getValue, Collectors.toSet())));
		System.out.println(map);
		
		}

}
