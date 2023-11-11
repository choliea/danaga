package com.danaga.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danaga.dto.CommentDto;
import com.danaga.service.CommentsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentsRestConrtoller {

	private final CommentsService cService;
	
	@PostMapping("/{boardId}/create_comment")
	public ResponseEntity<CommentDto> create (@PathVariable("boardId")Long boardId, @RequestBody CommentDto dto) {
		
		log.info("boardId : {}",boardId);
		log.info("dto : {}",dto);
		CommentDto comments = cService.createComment(dto,boardId);
		log.info("target : {}",comments);
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}
	
	@GetMapping("/{boardId}/comment_list")
	public ResponseEntity<List<CommentDto>> getConmments(@PathVariable("boardId")Long boardId){
		List<CommentDto> comments= cService.comments(boardId);
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}
	
	@PatchMapping("/{commentId}/edit_comment")
	public ResponseEntity<CommentDto> update(@PathVariable("commentId")Long commentId,@RequestBody CommentDto comment){
		CommentDto comments = cService.update(commentId, comment);
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}
	
	@DeleteMapping("/{commentId}/delete_comment")
	public ResponseEntity<CommentDto> update(@PathVariable("commentId")Long commentId ,@RequestBody String pw){
		CommentDto comments = cService.delete(commentId,pw);
		return (comments!=null)? ResponseEntity.status(HttpStatus.NO_CONTENT).body(comments) :
			ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
