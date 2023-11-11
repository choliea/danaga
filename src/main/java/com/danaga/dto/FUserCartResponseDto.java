package com.danaga.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danaga.dto.product.OptionDto;
import com.danaga.entity.OptionSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FUserCartResponseDto {
	private Integer qty; // 카트 수량
	private String productName; // 제품 이름
	private Integer totalPrice; // 토탈 금액
	private String pImage; // 프로덕트 사진
	private Long osId; // 옵션셋 아이디
	@Builder.Default
	private List<CartElseOptionsetDto> elseOptionSets = new ArrayList<>();

	@Builder.Default
	private List<CartOptionDto> options = new ArrayList<>(); // 옵션 네임 밸류

	public static FUserCartResponseDto toDto(OptionSet entity, Integer qty) {
		return FUserCartResponseDto.builder().pImage(entity.getProduct().getImg())
				.productName(entity.getProduct().getName()).osId(entity.getId()).qty(qty)
				.options(entity.getOptions().stream().map(t -> new CartOptionDto(t)).collect(Collectors.toList()))
				.totalPrice(entity.getTotalPrice()).build();

	}

}
