package com.danaga.dto;

import com.danaga.entity.LikeConfig;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeConfigDto {

	private Long id;
	private Long boardId;
	private String userName;
	private Integer isLike;
	private Integer disLike;
	
	public static LikeConfigDto createConfigDto(Long boardId, String userName) {
		
		return LikeConfigDto.builder()
				.boardId(boardId).userName(userName).isLike(0).disLike(0).build();
	}
	
	public static LikeConfigDto responsDto(LikeConfig config) {
		return LikeConfigDto.builder()
				.id(config.getId()).isLike(config.getIsLike()).disLike(config.getDisLike()).boardId(config.getBoard().getId()).userName(config.getMember().getUserName())
				.build();
		
	}
}
