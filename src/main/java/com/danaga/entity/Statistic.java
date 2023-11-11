package com.danaga.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
	@Id
	private String id;
	//일일 총 판매 수량
	private Long dailySalesTotQty;
	//일일 총 판매 수익
	private Long dailySalesRevenue;
	//일일 게시글 갯수
	private Long dailyBoardInquiry;
	//일일 신규멤버 숫자
	private Long dailyNewMember;
	
	

}
