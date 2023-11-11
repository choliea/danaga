package com.danaga.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.danaga.dto.BoardDto;
import com.danaga.entity.Board;
import com.danaga.entity.BoardGroup;
import com.danaga.entity.LikeConfig;
import com.danaga.entity.Member;
import com.danaga.repository.BoardGroupRepository;
import com.danaga.repository.BoardRepository;
import com.danaga.repository.LikeConfigRepository;
import com.danaga.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardRepository bRepository;
	
	@Autowired
	private MemberRepository mRepository;
	@Autowired
	private BoardGroupRepository bgRepository;
	@Autowired
	private LikeConfigRepository lcRepository;
	
	
	public String getBoardGroupName(Long boardGroupId) {
		List<BoardDto> dto = bRepository.findByBoardGroupIdOrderByCreateTimeDesc(boardGroupId).stream().map(t -> BoardDto.responseDto(t)).collect(Collectors.toList());
		String name ="";
		for (BoardDto boardDto : dto) {
			name =boardDto.getBoardGroupName();
			if(name!=null) {
				break;
			}
		}
		log.info("boardGroupName : {} ",name);
		return name;
	}
	//인기 게시물 출력
	@Override
	public List<BoardDto> popularPost(){
		List<BoardDto> top10List = bRepository.findTop10ByOrderByReadCountDesc().stream().map(t -> BoardDto.responseDto(t)).collect(Collectors.toList());
		for (BoardDto boardDto : top10List) {
			if(boardDto.getTitle().length()>10) {
				String contentTemp=boardDto.getTitle().substring(0,8)+"...";
				boardDto.setTitle(contentTemp);
			}
		}
		return top10List;
	}
	
	@Override
	public Page<BoardDto> boards(Pageable pageable,String searchKeyword,String searchType,Long boardGroupId){
		
		Specification<Board> specification=null;
		if(searchType !=null) {
			switch (searchType) {
			case "title": 
				specification=BoardSpecification.findBoardsWithBoardGroup(boardGroupId).and(BoardSpecification.containingTitle(searchKeyword));
				break;
			case "userName": 
				specification=BoardSpecification.findBoardsWithBoardGroup(boardGroupId).and(BoardSpecification.containingUserName(searchKeyword));
				break;
			case "content": 
				specification=BoardSpecification.findBoardsWithBoardGroup(boardGroupId).and(BoardSpecification.containingContent(searchKeyword));
				break;
			
			default:
				specification=BoardSpecification.findBoardsWithBoardGroup(boardGroupId);
			}
		}
		Page<BoardDto> boardList = bRepository.findAll(specification, pageable).map(BoardDto::responseDto);
		log.info("specification  : {}",specification);
		return boardList;
		
		
	}
	
	
	// 게시물별 출력
	@Override
	public List<BoardDto> boards(Long boardGroupId) {
		List<BoardDto> boardList=bRepository.findByBoardGroupIdOrderByCreateTimeDesc(boardGroupId).stream()
				.map(board -> BoardDto.responseDto(board)).collect(Collectors.toList());
		for (BoardDto boardDto : boardList) {
			if(boardDto.getContent().length()>5) {
				String contentTemp=boardDto.getContent().substring(0,5)+"...";
				boardDto.setContent(contentTemp);
			}
			if(boardDto.getTitle().length()>5) {
				String contentTemp=boardDto.getTitle().substring(0,5)+"...";
				boardDto.setTitle(contentTemp);
			}
		}
		return boardList;
	}

	//게시글 하나출력
	@Override
	public BoardDto boardDetail(Long id) {
		Board target = bRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("대상이없습니다."));
		target.readCountUp(target);
		bRepository.save(target);
		BoardDto temp = BoardDto.responseDto(target);
		return temp;
	}

	// 생성
	@Override
	@Transactional
	public BoardDto createBoard(BoardDto dto) {
	    // 게시글 조회 및 예외처리
	    Member memberT = mRepository.findByUserName(dto.getUserName())
	            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
	    BoardGroup boardGroupT = bgRepository.findById(dto.getBoardGroupId())
	            .orElseThrow(() -> new IllegalArgumentException("게시판을 찾을 수 없습니다."));
	    BoardDto create=BoardDto.createDto(dto);
	    log.info("create : {}",create);
		/*
		 * if (create.getId() != null) { throw new
		 * IllegalArgumentException("이미 존재하는 게시물ID입니다."); }
		 */
	    
	    // 게시글 엔티티 생성
	    Board board = Board.toEntity(create, memberT, boardGroupT);
	    log.info("board : {} ",board);
	    // 게시글 엔티티를 DB에 저장
	    Board created = bRepository.save(board);

	    // LikeConfig 엔티티 생성 및 저장
	    LikeConfig likeConfig = LikeConfig.createConfig(created, memberT);
	    lcRepository.save(likeConfig);

	    // 게시글 엔티티를 DTO로 변환 및 반환
	    return BoardDto.responseDto(board);
	}


	// 좋아요
	@Override
	@Transactional
	public BoardDto upIsLike(Long boardId, String userName) {
	    // board와 member config 객체 조회 및 예외처리
	    Board board = bRepository.findById(boardId)
	            .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다.(대상게시글이 없음)"));
	    Member member = mRepository.findByUserName(userName)
	            .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다.(회원정보없음)"));

	    // LikeConfig 객체 조회
	    Optional<LikeConfig> config = lcRepository.findByBoardAndMember(board, member);

	    LikeConfig obj;

	    if (config.isEmpty()) {
	        // 좋아요 상태가 없는 회원의 경우, 새로운 LikeConfig 객체 생성
	        obj = LikeConfig.createConfig(board, member);
	        obj.setIsLike(1); // 좋아요 상태를 1로 만들어줌
	        board.setIsLike(board.getIsLike()+1);
	    } else {
	        // 이미 좋아요 상태가 있는 경우, config 객체를 가져옴
	        obj = config.get();

	        // 현재 상태에 따라 좋아요 상태를 반전
	        obj.setIsLike(obj.getIsLike() == 0 ? 1 : 0);
	        if (obj.getIsLike() == 1) {
	            board.setIsLike(board.getIsLike() + 1); 
	        }else if (obj.getIsLike()==0) {
	        	board.setIsLike(board.getIsLike() -1);
	        }
	    }

	    // config 상태를 저장
	    lcRepository.save(obj);

	    // board entity상태 변경후 db에 갱신
	    board.updateConfig(obj);
	    Board response = bRepository.save(board);

	    // 저장후 dto객체로 변환 및 반환
	    return BoardDto.responseDto(response);
	}


	// 싫어요
	@Override
	@Transactional
	public BoardDto upDisLike(Long boardId, String userName) {
		 // board와 member config 객체 조회 및 예외처리
	    Board board = bRepository.findById(boardId)
	            .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다.(대상게시글이 없음)"));
	    Member member = mRepository.findByUserName(userName)
	            .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다.(회원정보없음)"));

	    // LikeConfig 객체 조회
	    Optional<LikeConfig> config = lcRepository.findByBoardAndMember(board, member);

	    LikeConfig obj;

	    if (config.isEmpty()) {
	        // 좋아요 상태가 없는 회원의 경우, 새로운 LikeConfig 객체 생성
	        obj = LikeConfig.createConfig(board, member);
	        obj.setDisLike(1); // 좋아요 상태를 1로 만들어줌
	        board.setDisLike(board.getDisLike()+1);
	    } else {
	        // 이미 좋아요 상태가 있는 경우, config 객체를 가져옴
	        obj = config.get();

	        // 현재 상태에 따라 좋아요 상태를 반전
	        obj.setDisLike(obj.getDisLike() == 0 ? 1 : 0);
	        if (obj.getDisLike() == 1) {
	            board.setDisLike(board.getDisLike() + 1); 
	        }else if (obj.getDisLike()==0) {
	        	board.setDisLike(board.getDisLike() -1);
	        }
	    }

	    // config 상태를 저장
	    lcRepository.save(obj);

	    // board entity상태 변경후 db에 갱신
	    board.updateConfig(obj);
	    Board response = bRepository.save(board);

	    // 저장후 dto객체로 변환 및 반환
	    return BoardDto.responseDto(response);
	}

	// 게시물수정
	@Override
	@Transactional
	public BoardDto update(BoardDto dto) {
		// 타겟 게시물 조회 및 예외처리
		Board target = bRepository.findById(dto.getId()).orElseThrow(() -> new IllegalArgumentException("대상이없습니다."));
		if(!target.getMember().getUserName().equals(dto.getUserName())) {
			throw new  IllegalArgumentException("회원이 일치하지 않습니다.");
		}
		// 댓글 수정
		target.patch(dto);

		// DB로 갱신
		Board updated = bRepository.save(target);

		// 게시물 엔티티를 DTO로 변환 및 반환
		return BoardDto.responseDto(updated);
	}

	// 게시물 삭제
	@Override
	@Transactional
	public BoardDto delete(BoardDto dto,String userName) {
		// config상태 삭제
		if(!dto.getUserName().equals(userName)) {
			throw new  IllegalArgumentException("회원이 일치하지 않습니다.");
			
		}
		lcRepository.deleteByBoard_Id(dto.getId());
		
		// 게시물 조회 및 예외처리
		Board target = bRepository.findById(dto.getId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
		if(dto.getUserName() == target.getMember().getUserName()) {
			// DTO와 entity의 멤버아이디가 같다면 게시물 DB에서 삭제
			bRepository.delete(target);
		}else {
			return null;
		}
		

		// 삭제된 댓글을 dto로 반환
		return BoardDto.responseDto(target);
	}
	
}
