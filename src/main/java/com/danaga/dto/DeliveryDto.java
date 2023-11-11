package com.danaga.dto;

import com.danaga.entity.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DeliveryDto {
    private String name;
    private String phoneNumber;
    private String address;
    private String detailAddress;
    private String postCode;
    
	public static DeliveryDto toDto(Delivery entity) {
		return DeliveryDto.builder()
				.name(entity.getName())
				.phoneNumber(entity.getPhoneNumber())
				.address(entity.getAddress())
				.detailAddress(entity.getDetailAddress())
				.postCode(entity.getPostCode())
				.build();
	}
}
// 클라이언트가 배달요청 입력한것 받는 데이터