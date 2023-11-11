package com.danaga.dto;



import com.danaga.entity.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DeliveryResponseDto {
	private Long id;
	private String name;	
	private String phoneNumber;    
	private String address;	 
	private Long orderId;

	public static DeliveryResponseDto toDto(Delivery entity) {
		return DeliveryResponseDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.phoneNumber(entity.getPhoneNumber())
				.address(entity.getAddress())
				.orderId(entity.getOrders().getId())
				.build();
	}
}
//클라이언트에게 배달접수받은거 보여주는 데이터