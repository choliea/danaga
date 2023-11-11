package com.danaga.dto;

import com.danaga.entity.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderGuestDto {
	private String name;
	private String phoneNo;
	private String email;

	
}
