package com.danaga.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDataDto {
	private Long totSales;
	private Long toSales;
	private Long failSales;

}
