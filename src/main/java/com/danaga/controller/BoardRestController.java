package com.danaga.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danaga.dto.BoardDto;
import com.danaga.service.BoardService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardRestController {

	private final BoardService bService;
	
	@PatchMapping("/{boardId}/upIsLike/{memberId}")
	public ResponseEntity<BoardDto> upIsLike(@PathVariable("boardId") Long boardId, HttpSession session) {
		
		log.info("boardId = {}",boardId);
		BoardDto dtos = bService.upIsLike(boardId,(String)session.getAttribute("sUserId"));
		log.info("dto = {}",dtos);
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
	
	@PatchMapping("/{boardId}/upDisLike/{memberId}")
	public ResponseEntity<BoardDto> upDisLike(@PathVariable("boardId") Long boardId, HttpSession session) {
		log.info("boardId",boardId);
		BoardDto dtos = bService.upDisLike(boardId,(String)session.getAttribute("sUserId"));
		log.info("dto = {}",dtos);
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
}
