package com.danaga.dto.product;

import java.util.Optional;

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
public class ProductUpdateDto {
	
	@NotNull
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String brand;
	@Min(0)
	private Integer price;
	private String descImage;
	private String prevImage;
	@NotBlank
	private String img;
	
	public Product toEntity() {
		return Product.builder()
				.id(id)
				.name(name)
				.brand(brand)
				.descImage(descImage)
				.prevImage(prevImage)
				.img(img)
				.price(price)
				.build();
	}
	
}
