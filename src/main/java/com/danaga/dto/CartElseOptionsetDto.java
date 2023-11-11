package com.danaga.dto;

import java.util.List;

import com.danaga.dto.product.OptionDto;
import com.danaga.dto.product.OtherOptionSetDto;
import com.danaga.dto.product.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartElseOptionsetDto {
	private Long osId;
	private String osString;
	private Integer totalPrice;
	private Boolean isInStock;
	public CartElseOptionsetDto(OtherOptionSetDto dto) {
		this.osId = dto.getOsId();
		this.totalPrice = dto.getTotalPrice();
		this.osString = dto.getOptionSetDesc();
		this.isInStock = dto.getIsInStock();
		//this.osString = optionset(dto.getOptionSet());
	}


	/*
	 * static String optionset(List<OptionDto> list) { String os = ""; if (list !=
	 * null) { for (int i = 0; i < list.size(); i++) { os += list.get(i).getName() +
	 * " : "; os += list.get(i).getValue() + " , "; } os = os.substring(0,
	 * os.length()); } return os; }
	 */

}
