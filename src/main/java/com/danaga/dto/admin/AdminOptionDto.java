package com.danaga.dto.admin;

import com.danaga.entity.Options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminOptionDto {
	
	private String name;
	private String value;
	private Integer extraPrice;
	
	public Options toEntity() {
		return Options.builder()
				.name(name)
				.value(value)
				.extraPrice(extraPrice)
				.build();
	}
	
}
