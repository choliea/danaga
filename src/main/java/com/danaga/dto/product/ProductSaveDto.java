package com.danaga.dto.product;

import com.danaga.entity.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveDto {
	@NotBlank
	@NotNull
	private String name;
	@NotBlank
	private String brand;
	@NotNull
	@Min(1)
	private Integer price;
	private String descImage;
	private String prevImage;
	@NotNull
	private String img;
	
	public Product toEntity() {
		return Product.builder()
				.name(name)
				.brand(brand)
				.descImage(descImage)
				.prevImage(prevImage)
				.img(img)
				.price(price)
				.build();
	}
	
}
