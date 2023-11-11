package com.danaga.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.danaga.dto.BoardDto;
import com.danaga.entity.Board;
import com.danaga.entity.BoardGroup;
import com.danaga.entity.Member;
import com.danaga.repository.BoardGroupRepository;
import com.danaga.repository.BoardRepository;
import com.danaga.repository.MemberRepository;

@SpringBootTest
public class BoardServiceImplTest {

    @Autowired
    private BoardServiceImpl boardService;

    //@Test
    /*public void testCreateBoard() {
        // Mock necessary data
        BoardDto boardDto = BoardDto.builder()
                .boardGroupId(1L)
                .title("test title")
                .content("test content")
                .memberId(7L)
                .build();

       
        
        BoardDto result = boardService.createBoard(boardDto);
        System.out.println(">>>>>>>>board : "+result);
        
    }*/
}



