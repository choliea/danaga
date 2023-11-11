package com.danaga.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.danaga.service.StatisticService;

@Component
public class StatisticScheduling {
	@Autowired
	private StatisticService statisticService;
	
	@Scheduled(cron="0 0 0/1 * * *")
	public void dailyUpdate() {
		//매일 자정 금월 데이터 업데이트
		statisticService.updateLatest7Days();
	}
	
	@Scheduled(cron="00 00 09 1 * *")
	public void monthlyUpdateStart() {
		//월초 당월 M데이터 생성
		statisticService.createMoData(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
	}
	
	@Scheduled(cron="00 00 09 10 * *")
	public void monthlyUpdateEnd() {
		//M월 마감 실행
		statisticService.updateLastMonth();
		statisticService.createMoData(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM")));
	}
	
	
}
