package com.danaga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.danaga.entity.BoardGroup;

public interface BoardGroupRepository extends JpaRepository<BoardGroup, Long>,JpaSpecificationExecutor<BoardGroup>{

}
