package com.danaga.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danaga.dto.product.ProductDto;
import com.danaga.entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SUserCartResponseDto {
	private Long id;
	private Long memberId;

	private Integer qty;
	private String productName;
	private Integer totalPrice; // 토탈 금액
	private String pImage; // 프로덕트 사진
	private Long osId; // 옵션셋 아이디
	private Integer grade;
	@Builder.Default
	private List<CartElseOptionsetDto> elseOptionSets = new ArrayList<>();
	@Builder.Default
	private List<CartOptionDto> options = new ArrayList<>(); // 옵션 네임 밸류

	public static SUserCartResponseDto toDto(Cart entity) {

		return SUserCartResponseDto.builder().id(entity.getId()).memberId(entity.getMember().getId())
				.qty(entity.getQty()).productName(entity.getOptionSet().getProduct().getName())
				.totalPrice(entity.getOptionSet().getTotalPrice()).pImage(entity.getOptionSet().getProduct().getImg())
				.osId(entity.getOptionSet().getId()).grade(gradePoint(entity.getMember().getGrade()))
				.options(entity.getOptionSet().getOptions().stream().map(t -> new CartOptionDto(t))
						.collect(Collectors.toList()))
				.build();
	}

	static Integer gradePoint(String value) {
		String grade = value;
		Integer gradePoint = 0;
		switch (grade) {
		case "Rookie": {
			gradePoint = 1;
			break;
		}
		case "Bronze": {
			gradePoint = 4;
			break;
		}
		case "Silver": {
			gradePoint = 7;
			break;
		}
		case "Gold": {
			gradePoint = 10;
			break;
		}
		case "Platinum": {
			gradePoint = 13;
			break;
		}
		case "Diamond": {
			gradePoint = 16;
			break;
		}
		}
		return gradePoint;
	}
}
