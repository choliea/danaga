package com.danaga.dto;

import com.danaga.entity.Member;
import com.danaga.entity.OptionSet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class CartUpdateOptionSetDto {
	private Long id;
	private OptionSet optionSet;
}
