package com.danaga.dto.admin;

import java.util.List;

import com.danaga.dto.product.ProductSaveDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminOptionSetDto {
	private List<AdminOptionDto> optionsDto;
	private Long productNo;
	private Integer osStock;

}
