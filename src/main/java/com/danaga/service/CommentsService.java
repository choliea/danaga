package com.danaga.service;

import java.util.List;

import com.danaga.dto.CommentDto;

public interface CommentsService {

	List<CommentDto> comments(Long boardId);
	CommentDto createComment(CommentDto commentDto,Long boardId);
	CommentDto update(Long id, CommentDto dto);
	CommentDto delete(Long id,String pw);
}
