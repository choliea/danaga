package com.danaga.service;

import org.springframework.data.jpa.domain.Specification;

import com.danaga.entity.Board;
import com.danaga.entity.BoardGroup;
import com.danaga.entity.Member;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BoardSpecification {

	public static Specification<Board> findAll(Long boardGroupId) {
		return new Specification<Board>() {
			
			@Override
			public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("boardGroupId"), boardGroupId);
			}
		};
	}
	
	public static Specification<Board> containingTitle(String title) {
		return new Specification<Board>() {
			
			@Override
			public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("title"), "%"+title+"%");
			}
		};
	}
	
	public static Specification<Board> containingUserName(String userName) {
	    return new Specification<Board>() {
	        @Override
	        public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	            Join<Board, Member> memberJoin = root.join("member", JoinType.INNER); // "member"는 Board 엔티티의 필드명

	            return criteriaBuilder.like(memberJoin.get("nickname"), "%" + userName + "%");
	        }
	    };
	}
	public static Specification<Board> containingContent(String content) {
		return new Specification<Board>() {
			
			@Override
			public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("content"), "%"+content+"%");
			}
		};
	}

	public static Specification<Board> findBoardsWithBoardGroup(Long boardGroupId) {
		// TODO Auto-generated method stub
		return (root, query, criteriaBuilder) -> {
	        Join<Board,BoardGroup> boards = root.join("boardGroup");
	        return criteriaBuilder.equal(boards.get("id"), boardGroupId);
	    };
	}
}
