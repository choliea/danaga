package com.danaga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danaga.dto.BoardDto;
import com.danaga.dto.LikeConfigDto;
import com.danaga.entity.LikeConfig;
import com.danaga.repository.BoardRepository;
import com.danaga.repository.LikeConfigRepository;
import com.danaga.repository.MemberRepository;

@Service
public class LikeConfigService {

	@Autowired
	private LikeConfigRepository lcRepository;
	@Autowired
	private MemberRepository mRepository;
	@Autowired
	private BoardRepository bRepository;

	public void deleteConfigs(BoardDto dto) { 
		// 대상찾기 
		LikeConfig target =lcRepository.findByBoard_IdAndMember_UserName(dto.getId(),dto.getUserName()).orElseThrow(() -> new IllegalArgumentException("존재하지않는 상태입니다."));
		//대상삭제
		lcRepository.delete(target); 
		
		
	}

}
