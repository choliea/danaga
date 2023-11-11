package com.danaga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.danaga.dto.BoardDto;
import com.danaga.dto.CommentDto;
import com.danaga.entity.Board;
import com.danaga.service.BoardService;
import com.danaga.service.CommentsService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {

	@Autowired
	private BoardService bService;
	@Autowired
	private CommentsService cService;
	
	
	@GetMapping("/board{boardGroupId}")
	public String list(@PathVariable(name = "boardGroupId") Long boardGroupId,
			@RequestParam(name = "searchKeyword", required = false, defaultValue = "") String searchKeyword,
			@RequestParam(name = "searchType", required = false, defaultValue = "none") String searchType,
			@Parameter(hidden = true) @PageableDefault(page = 1, size = 5) Pageable p, Model model) {
		int page = p.getPageNumber();
		int size = p.getPageSize();
		int blockSize = 5;
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createTime").descending());

		Page<BoardDto> boardList = bService.boards(pageable, searchKeyword, searchType, boardGroupId);

		log.info("boardList : {} ",boardList.getContent()); 
		List<BoardDto> top10List =bService.popularPost(); 
		log.info("top10List : {} ",top10List); 
		String name =bService.getBoardGroupName(boardGroupId);
		model.addAttribute("boardGroupId",boardGroupId);
		model.addAttribute("boardGroupName",name);
		model.addAttribute("boardList",boardList);
		model.addAttribute("top10",top10List); 
		
		return "board/board_list";
		
	}
	
	 @GetMapping("/board_create{boardGroupId}") public String
	 createForm(@PathVariable Long boardGroupId ,Model model,HttpSession session)
	 { log.info("session : {} ",session.getAttribute("sUserId"));
	 model.addAttribute("boardGroupId",boardGroupId);
	 model.addAttribute("board",new BoardDto()); return "board/create_board"; }
	 

	@PostMapping("/board_create{boardGroupId}")
	public String createBoard(@PathVariable("boardGroupId") Long boardGruopId, @ModelAttribute("board") BoardDto dto,
			Model model, HttpSession session) {
		log.info("dto : {} ", dto);
		String user = (String) session.getAttribute("sUserId");
		log.info("세션정보 : {} ", user);
		BoardDto saved = bService.createBoard(dto);
		log.info("saved: {}", saved);

		model.addAttribute("saved", saved);
		model.addAttribute("msg", "새로운 글이 생성 되었습니다.");
		return "redirect:/show" + saved.getId();
	}

	@GetMapping("/show{id}")
	public String show(@PathVariable Long id, Model model) {
		BoardDto board = bService.boardDetail(id);
		List<CommentDto> comments = cService.comments(id);
		model.addAttribute("comments", comments);
		model.addAttribute("board", board);
		return "board/board_detail";
	}

	@GetMapping("/board_edit{id}")
	public String edit(@PathVariable Long id, Model model) {
		BoardDto board = bService.boardDetail(id);
		log.info("board : {} ",board);
		model.addAttribute("board", board);

		return "board/edit_board";
	}

	@PostMapping("/board_edit{id}")
	public String updated(@PathVariable Long id, BoardDto dto, Model model, RedirectAttributes rttr) {
		BoardDto board = bService.boardDetail(id);
		log.info("board : {} ",board);
		board = bService.update(dto);
		model.addAttribute("board", board);
		rttr.addFlashAttribute("upd", "수정이 완료 되었습니다.");
		return "redirect:/show"+ board.getId();
	}

	@GetMapping("/board_delete{id}")
	public String delete(@PathVariable Long id, RedirectAttributes rttr, HttpSession session) {
		// 1. 삭제 대상을 가져온다~
		BoardDto target = bService.boardDetail(id);
		log.info("target : {} ",target);
		String user = (String) session.getAttribute("sUserId");
		// 2. 대상을 삭제한다~
		if (target != null) {
			bService.delete(target, user);
			rttr.addFlashAttribute("msg", "삭제가 완료 되었습니다.");
		}
		// 3. 결과페이지로 리다이렉트한다.
		return "redirect:/board" + target.getBoardGroupId();
	}

}
