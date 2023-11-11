package com.danaga.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danaga.entity.Board;
import com.danaga.entity.BoardGroup;
import com.danaga.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;




@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardDto {
	private Long id;
    private String title;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private Integer isLike;
    private Integer disLike;
    private Integer readCount;
    private Integer isAdmin;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String nickname;
    private String boardGroupName;
    private String userName;
    private Long boardGroupId;
    @Builder.Default
    private List<LikeConfigDto> lConfigs = new ArrayList<>();
    
    public static BoardDto createDto(BoardDto dto) {
    	return BoardDto.builder()
    			.id(dto.getId()).title(dto.getTitle()).content(dto.getContent()).img1(dto.getImg1())
    			.img2(dto.getImg2()).img3(dto.getImg3()).img4(dto.getImg4()).img5(dto.getImg5())
    			.isLike(0).disLike(0).readCount(0).isAdmin(1)
    			.createTime(dto.getCreateTime()).nickname(dto.getNickname())
    			.userName(dto.getUserName()).boardGroupId(dto.getBoardGroupId())
    			.build();
    }
    
    public static BoardDto responseDto(Board board) {
    	if(board.getMember().getRole().equals("ADMIN")) {
    		board.setIsAdmin(2);
    	}
    	return BoardDto.builder()
    			.id(board.getId()).title(board.getTitle()).content(board.getContent()).img1(board.getImg1())
    			.img2(board.getImg2()).img3(board.getImg3()).img4(board.getImg4()).img5(board.getImg5())
    			.isLike(board.getIsLike()).disLike(board.getDisLike()).readCount(board.getReadCount()).isAdmin(board.getIsAdmin())
    			.createTime(board.getCreateTime()).nickname(board.getMember().getNickname())
    			.userName(board.getMember().getUserName()).boardGroupId(board.getBoardGroup().getId())
    			.boardGroupName(board.getBoardGroup().getName())
    			.lConfigs(board.getLConfigs().stream().map(t -> LikeConfigDto.responsDto(t)).collect(Collectors.toList())).build();
    }

}
