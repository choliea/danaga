package com.danaga.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.danaga.entity.Comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	private Long id;
	private String writer;
	private String content;
	private String pw;
	private Long parentId;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private Long boardId;
	
	public static CommentDto createDto(CommentDto comment) {
	  

	    return CommentDto.builder()
	            .id(0L)
	            .writer(comment.getWriter())
	            .content(comment.getContent())
	            .pw(comment.getPw())
	            .createTime(comment.getCreateTime())
	            .parentId(comment.getParentId())
	            .boardId(comment.getBoardId())
	            .updateTime(comment.getUpdateTime())
	            .build();
	}

	
	public static CommentDto responseDto(Comments comment) {
	    CommentDto.CommentDtoBuilder builder = CommentDto.builder()
	            .id(comment.getId())
	            .writer(comment.getWriter())
	            .content(comment.getContent())
	            .pw(comment.getPw())
	            .createTime(comment.getCreateTime())
	            .updateTime(comment.getUpdateTime())
	            .boardId(comment.getBoard().getId());

	    if (comment.getParent() != null) {
	        builder.parentId(comment.getParent().getId());
	    }

	    return builder.build();
	}

	
}
