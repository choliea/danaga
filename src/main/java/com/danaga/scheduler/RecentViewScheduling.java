package com.danaga.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.danaga.service.product.RecentViewService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecentViewScheduling {
	private final RecentViewService service;
	
	@Scheduled(cron="0 0 0 * * *")
	public void dailyUpdate() {
		//매일 자정 30일 지난 최근 뷰들 삭제
		service.removeOldRecents();
	}
}
