package com.danaga.dto;


import com.danaga.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class KakaoMemberUpdateDto {
	private Long id;
	private String userName;
	private String password;
	private String nickname;
	private String postCode;
	private String address;
	private String detailAddress;
	private String role;
	
	public static KakaoMemberUpdateDto toDto(Member entity) {
    	return KakaoMemberUpdateDto.builder()
    			.id(entity.getId())
    			.userName(entity.getUserName())
    			.password(entity.getPassword())
    			.nickname(entity.getNickname())
    			.postCode(entity.getPostCode())
    			.address(entity.getAddress())
    			.detailAddress(entity.getDetailAddress())
    			.role(entity.getRole())
    			.build();
    }
}
