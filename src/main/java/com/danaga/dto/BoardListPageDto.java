package com.danaga.dto;

import org.springframework.data.domain.Page;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/*

게시판타이틀80		김경호80	2023-11-02	0
게시판타이틀80		김경호80	2023-11-02	0
게시판타이틀80		김경호80	2023-11-02	0
---------------------------------------------| 페이지블록
      (1) 2  3  4  5  6  7  8  9 10  ▶  ▶▶  |   1번 
   ◀   1 (2) 3  4  5  6  7  8  9 10  ▶  ▶▶  |   1번 
◀◀ ◀  11 12 13 14 15(16)17 18 19 20  ▶  ▶▶  |   2번
◀◀ ◀  21 22 23 24 25 26 27 28 29 30  ▶  ▶▶  |   3번
◀◀ ◀  31 32 33(34)35                 ▶      |   4번
◀◀ ◀  31 32 33 34(35)                       |   4번

*/
@Getter
@Setter
public class BoardListPageDto{
	private String searchKeyword;
	private String searchType;
	private Page page;
	/*
	01.한페이지당 게시물 수(page.pageSize())
	02.현재페이지번호(page.pageNumber())	
	03.전체게시물수(page.getTotalElements())
	04.이전페이지번호(page.previousPageable())
	05.다음페이지번호(page.nextPageable())		
	06.전체페이지수(page.getTotalPages())
	 */
	
	private int blockSize;      	//페이지블록안에있는페이지수
	private int totalPages;		  	//전체페이지수
	private int totalCount;  		//전체게시물수
	private int currentBlock;     	//현재페이지블록번호
	private int blockBeginPage;   	//현재페이지블록의 시작페이지번호
	private int blockEndPage;   	//현재페이지블록의 시작페이지번호
	private int prevGroupStartPage; //이전페이지블록의 시작 페이지번호
	private int nextGroupStartPage; //다음페이지블록의 마지막페이지번호
	
	public BoardListPageDto( Page page,int blockSize){
		this.searchKeyword="";
		this.searchType="none";
		this.page=page;
		/***페이지블록안에있는페이지수**/
		this.blockSize=blockSize;
		this.currentBlock = 1; // 현재 페이지 블록 번호
		/*################블록 계산부분추가######################**/
		/** 현재 페이지가 몇번째 페이지 블록에 속하는지 계산**/
		this.currentBlock = (int) Math.ceil((page.getNumber()) / blockSize) + 1;
		/** 현재 페이지 블록의 시작페이지 계산**/
		this.blockBeginPage = (currentBlock - 1) * blockSize + 1;
		/** 현재 페이지 블록의 끝  페이지 계산**/
		this.blockEndPage = ((blockBeginPage + blockSize - 1)>page.getTotalPages())? page.getTotalPages():(blockBeginPage + blockSize - 1);
		
		/** 이전페이지블록의 시작 페이지번호**/
		this.prevGroupStartPage = blockBeginPage - blockSize;
		/** 다음페이지블록의 시작 페이지번호**/
		this.nextGroupStartPage = blockBeginPage + blockSize;
		/*#######################################################**/
		System.out.println("***************페이지정보***********************************");
		System.out.println("01.한페이지당 게시물 수(page.pageSize())		:"+page.getSize());
		System.out.println("02.현재페이지번호(page.pageNumber())			:"+page.getNumber());
		System.out.println("03.전체게시물수(page.getTotalElements())		:"+page.getTotalElements());
		System.out.println("04.이전페이지번호(page.previousPageable())		:"+(page.previousPageable().isPaged()?page.previousPageable().getPageNumber():"이전페이지존재안함"));
		System.out.println("05.다음페이지번호(page.nextPageable())			:"+(page.nextPageable().isPaged()?page.nextPageable().getPageNumber():"다음페이지존재안함"));
		System.out.println("06.전체페이지수(page.getTotalPages())			:"+page.getTotalPages());
		System.out.println("07.페이지블록안에있는페이지수(blockSize)		:"+this.blockSize);
		System.out.println("08.현재페이지블록번호(curBlock)					:"+this.currentBlock);
		System.out.println("09.현재페이지블록시작 페이지번호(blockBeginPage):"+this.blockBeginPage);
		System.out.println("10.현재페이지블록  끝 페이지번호(blockEndPage)	:"+this.blockEndPage);
		System.out.println("11.이전블록시작페이지번호(prevGroupStartPage)	:"+this.prevGroupStartPage);
		System.out.println("12.다음블록시작페이지번호(nextGroupStartPage)	:"+this.nextGroupStartPage);
		System.out.println("*************************************************************");
	}
	
	public BoardListPageDto( Page page,int blockSize,String searchKeyword,String searchType){
		this(page,blockSize);
		this.searchKeyword=searchKeyword;
		this.searchType=searchType;
	}
	
}