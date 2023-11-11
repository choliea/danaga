package com.danaga.dto.product;

import com.danaga.entity.Interest;
import com.danaga.entity.Member;
import com.danaga.entity.OptionSet;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterestDto {
	//관심상품 추가에 필요한 데이터
	//수정은 필요 없고 삭제는 pk만 있으면 됨
	@NotNull
	private Long memberId;
	
	@NotNull
	private Long optionSetId;
	
	public Interest toEntity(InterestDto dto) {
		Interest entity = Interest.builder().member(Member.builder().id(dto.getMemberId()).build()).optionSet(OptionSet.builder().id(dto.getOptionSetId()).build()).build();
		return entity;
	}
	public InterestDto(Interest entity){
		this.memberId=entity.getMember().getId();
		this.optionSetId=entity.getOptionSet().getId();
	}
}
