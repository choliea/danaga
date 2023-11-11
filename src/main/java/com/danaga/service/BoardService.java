package com.danaga.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.danaga.dto.BoardDto;
import com.danaga.entity.Board;

public interface BoardService {
	List<BoardDto> popularPost();
	
	List<BoardDto> boards(Long boardGroupId);
	
	Page<BoardDto> boards(Pageable pageable,String searchKeyword,String searchType,Long boardGroupId);
	
	BoardDto boardDetail(Long id);
	
	BoardDto createBoard(BoardDto dto);
	
	BoardDto upIsLike(Long boardId, String userName);
	
	BoardDto upDisLike(Long boardId, String userName);
	
	BoardDto update(BoardDto dto);
	
	BoardDto delete(BoardDto dto,String userName);
	String getBoardGroupName(Long boardGroupId);
}
