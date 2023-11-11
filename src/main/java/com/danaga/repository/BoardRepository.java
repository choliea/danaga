package com.danaga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.danaga.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>,JpaSpecificationExecutor<Board>{

	/*
	 
	 			pk 분류 
	 			1. 자유게시판
	 			2. 1:1문의
	 			3. FAQ
	 			4. 공지
			
			like_config
				ex ) pk :1 + status:1 = 유저OO의 0번 게시물에 좋아요누른상태 
				ex ) pk :1 + status:2 = 유저OO의 0번 게시물에 좋아요누른상태 
	 */
	//인기 게시물 탑 10
	List<Board> findTop10ByOrderByReadCountDesc();
	
	//게시판 별로 게시물 리스트 뽑아주기.
	List<Board> findByBoardGroupIdOrderByCreateTimeDesc(Long boardGroupId);

	//하나의 게시판에 하나의 게시물디테일
	Board findByBoardGroup_IdAndId(Long boardGroupId, Long boardId);

	//게시물 삭제
	void deleteByBoardGroup_IdAndId(Long boardGroupId, Long boardId);
	
	List<Board> findByIsAdmin(Integer isAdmin);
}