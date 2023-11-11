package com.danaga.dto.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadProductDto {
	private ProductSaveDto product;
	private List<OptionDto> options;
	private OptionSetCreateDto optionSet;
}