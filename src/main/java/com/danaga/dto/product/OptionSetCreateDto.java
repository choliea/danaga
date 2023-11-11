package com.danaga.dto.product;

import com.danaga.entity.OptionSet;
import com.danaga.entity.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionSetCreateDto {
	@Min(1)
	@NotNull
	private Integer stock;
	@NotNull
	private Long productId;
	@NotNull
	private Integer productPrice;
	
	public OptionSet toEntity() {
		return OptionSet.builder()
				.stock(stock)
				.product(Product.builder().id(productId).build())
				.totalPrice(productPrice)
				.build();
	}

	
}
