package com.danaga.dto.product;

import java.util.ArrayList;
import java.util.List;

import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionSetUpdateDto {
	@NotNull
	private Long id;
	@Min(0)
	private Integer stock;
	@Builder.Default
	private List<OptionDto> options=new ArrayList<>();
	public OptionSetUpdateDto(OptionSet createdOptionSet) {
		this.id = createdOptionSet.getId();
		this.stock = createdOptionSet.getStock();
		List<Options> list = createdOptionSet.getOptions();
		List<OptionDto> optionDtos = new ArrayList<>();
		for (int i=0; i<list.size();i++) {
			optionDtos.add(new OptionDto(list.get(i)));
		}
		options = optionDtos;
	}
	public OptionSet toEntity() {
		List<Options> optionsEntityList = new ArrayList<>();
		
		for (int i = 0; i < options.size(); i++) {
			optionsEntityList.add(options.get(i).toEntity());
		}
		return OptionSet.builder()
				.id(id)
				.stock(stock)
				.options(optionsEntityList)
				.build();
	}
}
