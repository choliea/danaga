package com.danaga.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckedDataDto {
	private Long id;
	private Integer qty;
	private String productName;
	private Integer totalPrice; 
}
