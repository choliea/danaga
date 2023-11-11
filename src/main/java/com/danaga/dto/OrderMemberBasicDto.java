package com.danaga.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderMemberBasicDto {
	private String userName;
	private String phoneNo;
	private String email;
}
