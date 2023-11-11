package com.danaga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.danaga.entity.Board;
import com.danaga.entity.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Long>{

	List<Comments> findByBoard_Id(Long boardId);
	
	@Query(nativeQuery = true, value = "SELECT c.* FROM comments c " +
            "LEFT JOIN comments parent ON c.parent_id = parent.comment_id " +
            "WHERE c.board_id = :boardId " +
            "ORDER BY parent.comment_id ASC NULLS FIRST, c.created_at ASC")
	List<Comments> findAllByBoard(Board board);

	List<Comments> findAllByParentId(Long id);
	
	void deleteAllByBoard_Id(Long boardId);
}
