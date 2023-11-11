package com.danaga.dto.product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danaga.dto.product.OptionDto.OptionBasicDto;
import com.danaga.entity.OptionSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtherOptionSetDto {//상세페이지 다른옵션 보여줄때
	private Long osId;
	@Builder.Default
	private List<OptionBasicDto> optionSet = new ArrayList<>();
	@Builder.Default
	private Boolean isInStock = true;
	private String optionSetDesc;
	private Integer totalPrice;
	public OtherOptionSetDto(OptionSet entity){
		this.optionSet = entity.getOptions().stream().map(t -> new OptionBasicDto(t)).collect(Collectors.toList());
		this.isInStock = true;
		this.osId = entity.getId();
		this.totalPrice=entity.getTotalPrice();
		if(entity.getStock()<=0) {
			this.isInStock=false;
		}
		StringBuilder sb = new StringBuilder();
		for (OptionBasicDto option : this.optionSet) {
		    sb.append(option.getName()+":"+option.getValue());
		        sb.append("/"); // 나머지 값은 '/'
		}
		String result = sb.toString();
		if (result.endsWith("/")) {
		    result = result.substring(0, result.length() - 1); // 마지막 '/' 제거
		}
		this.optionSetDesc=result;
	}
}
