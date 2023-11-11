package com.danaga.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danaga.dto.CommentDto;
import com.danaga.dto.ResponseDto;
import com.danaga.entity.Board;

import com.danaga.entity.Comments;
import com.danaga.repository.BoardRepository;
import com.danaga.repository.CommentsRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CommentsServiceImpl implements CommentsService{

	@Autowired
	private CommentsRepository cRepository;

	@Autowired
	private BoardRepository bRepository;

	@Override
	public List<CommentDto> comments(Long boardId) {
		return cRepository.findByBoard_Id(boardId).stream().map(comments -> CommentDto.responseDto(comments))
				.collect(Collectors.toList());
	}
	@Override
	@Transactional
	public CommentDto createComment(CommentDto commentDto, Long boardId) {
	    // 게시글 조회 및 예외 발생
	    Board board = bRepository.findById(boardId)
	            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

	    Comments parent = null;

	    // 자식 댓글인 경우
	    if (commentDto.getParentId() != null && commentDto.getParentId() != 0L) {
	        parent = cRepository.findById(commentDto.getParentId())
	                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상위 댓글입니다."));

	        // 부모댓글의 게시글 번호와 자식댓글의 게시글 번호가 같은지 체크하기
	        if (!parent.getBoard().getId().equals(commentDto.getBoardId())) {
	            throw new IllegalArgumentException("존재하지 않는 게시물입니다.");
	        }
	    } 

	    // 댓글 엔티티 생성
	    Comments target = Comments.toEntity(commentDto, board, parent);

	    // 댓글 엔티티 DB에 저장
	    Comments savedComment = cRepository.save(target);

	    // 엔티티를 DTO로 변환 및 반환
	    CommentDto commentResponseDto = CommentDto.responseDto(savedComment);
	    return commentResponseDto;
	}




	@Override
	@Transactional
	public CommentDto update(Long id, CommentDto dto) {
		// 댓글 조회 및 예외
		Comments target = cRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다."));

		if (dto.getPw() == target.getPw()) {
			throw new IllegalArgumentException("댓글 수정 실패! 대상 게시글의 비밀번호가 틀립니다.");
		}
		// 댓글 수정
		target.update(dto);
		// DB로 갱신
		Comments updated = cRepository.save(target);
		// 댓글 엔티티를 DTO로 변환 및 반환
		CommentDto response=CommentDto.responseDto(updated);
		return response;
	}
	@Override
	@Transactional
	public CommentDto delete(Long id,String pw) {
		// 댓글 조회 및 예외
		Comments target = cRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! 대상 댓글을 찾을수 없습니다."));
		
		//댓글 비밀번호 확인후 틀리면 null 리턴, 맞으면 삭제
		if(target.getPw()!=pw) {
			return null;
		}
		// 댓글을 DB에서 삭제
		cRepository.delete(target);
		// 삭제 댓글을 DTO로 반환
		CommentDto response=CommentDto.responseDto(target);
		return response;
	}
	
	
}
