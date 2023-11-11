package com.danaga.dto;

import com.danaga.dto.product.ProductDto;
import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class CartOptionDto {
	private String name ;
	private String value ;

	
	public CartOptionDto(Options entity) {
		this.name=entity.getName();
		this.value=entity.getValue();
	}
	
}
