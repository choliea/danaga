package com.danaga.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.danaga.entity.Board;
import com.danaga.repository.BoardRepository;
@Repository
public class BoardDaoImpl implements BoardDao{

	/* 미사용
	 * @Autowired private BoardRepository bRepository; //공통 작업 게시판 별로 출력 public
	 * List<Board> findWhichBoards(Long boardGroupId){ return
	 * bRepository.findByBoardGroupIdOrderByCreateTime(boardGroupId); } //해당 게시판 하나의
	 * 게시글 출력 public Board boardDetail(Long boardGroupId,Long boardId) { return
	 * bRepository.findByBoardGroup_IdAndId(boardGroupId, boardId); }
	 */
	
	/*
	  자유게시판 C.R.U.D
	   - 게시글 작성시 관리자만 답변가능
	      - 사용자
	         1.자신의 게시글 한하여 조회 등록 수정 삭제
	         2.자신의 게시글 답변 기능 있음
	      - 관리자는 모든 게시글 조회 등록 수정 삭제
         	1.사용자 게시글 답변 기능 있음
	 */
	
	
	
	
	
	/*
	 *  FAQ & 공지사항 C.R.U.D
	 	- 관리자가 관리하는 게시판(사용자가 자주묻는 질문확인)
	      - 사용자
	         1.조회만,파일 다운로드 가능
	      - 관리자
	         1.조회 등록 수정 삭제 파일첨부 필요
	 */
	
	
	/*
	 *  1:1문의 C.R.U.D
	 	- 등록은 로그인회원만(유저롤) 댓글은 아무나(게스트 롤)
		      - 사용자
		         1.사진,동영상URL 첨부 가능
		         2.조회 등록 수정 삭제
		         3.답변기능 ㅇ
		         4.대댓글 기능 ㅇ
		         5.댓글 알림기능 ㅇ
		      - 관리자
		         1.사진,동영상URL 첨부 가능
		         2.조회 등록 수정 삭제
		         3.답변기능 ㅇ
		         4.대댓글 기능 ㅇ
	 */
	
}