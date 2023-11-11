package com.danaga.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danaga.entity.Board;
import com.danaga.entity.LikeConfig;
import com.danaga.entity.Member;

public interface LikeConfigRepository extends JpaRepository<LikeConfig, Long>{

	Optional<LikeConfig> findByBoard_IdAndMember_UserName(Long boardId,String userName);
	
	Optional<LikeConfig> findByBoardAndMember(Board board, Member member);	
	
	void deleteByBoard_Id(Long boardId);
	
}
