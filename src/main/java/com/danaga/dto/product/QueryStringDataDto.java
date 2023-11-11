package com.danaga.dto.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryStringDataDto {
	
	private String orderType;
	@NotEmpty
	@Builder.Default
	private List<OptionDto.OptionNameValueMapDto> optionset=new ArrayList<OptionDto.OptionNameValueMapDto>();
	private Integer minPrice;
	private Integer maxPrice;
	private String nameKeyword;
	@NotBlank
	private CategoryDto category;
	@Builder.Default
	private Integer firstResult=0;
	
}
