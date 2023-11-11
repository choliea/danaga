package com.danaga.dto.admin;

import com.danaga.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductDto {
	
	private String name;
	private String brand;
	private Integer price;
	private String descImage;
	private String img;
	private String prevImage;
	private Integer stock;
	
	public Product toEntity() {
		return Product.builder()
				.name(name)
				.brand(brand)
				.descImage(descImage)
				.img(img)
				.prevImage(prevImage)
				.price(price)
				.build();
	}
	
}
