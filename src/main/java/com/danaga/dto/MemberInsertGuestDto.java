package com.danaga.dto;

import com.danaga.entity.Member;

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
public class MemberInsertGuestDto {
	private Long id;
	private String name;
	private String phoneNo;
	private String userName;
	private String email;
	@Builder.Default
	private String role = "Guest";
	
	public static MemberInsertGuestDto toDto(Member entity) {
    	return MemberInsertGuestDto.builder()
    			.id(entity.getId())
    			.name(entity.getName())
    			.phoneNo(entity.getPhoneNo())
    			.userName(entity.getUserName())
    			.role(entity.getRole())
    			.email(entity.getEmail())
    			.build();
    }
}

