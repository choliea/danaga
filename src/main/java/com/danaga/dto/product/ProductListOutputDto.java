package com.danaga.dto.product;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danaga.dto.product.OptionDto.OptionBasicDto;
import com.danaga.entity.Category;
import com.danaga.entity.CategorySet;
import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;
import com.danaga.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductListOutputDto {//리스트,히트상품,관심,최근상품리스트
	private String brand;
	private String name;
	private String updateTime;
	private String pImage;
	private Integer totalPrice;
	private String totalPriceString;
	private Long osId;
	@Builder.Default
	private List<OptionDto.OptionBasicDto> optionSet = new ArrayList<>();
	private Boolean isInterested;
	private String optionSetDesc;
	
	
	public ProductListOutputDto(OptionSet entity) {
		this.totalPriceString=new DecimalFormat("#,###").format(entity.getTotalPrice());
		this.brand=entity.getProduct().getBrand();
		this.name=entity.getProduct().getName();
		this.totalPrice = entity.getTotalPrice();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.updateTime = entity.getUpdateTime().format(formatter);
		this.pImage=entity.getProduct().getImg();
		this.osId=entity.getId();
		this.optionSet = entity.getOptions().stream().map(t -> new OptionDto.OptionBasicDto(t)).collect(Collectors.toList());
		this.isInterested=false;
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
	public ProductListOutputDto(OptionSet entity, String username) {
		this.totalPrice = entity.getTotalPrice();
		this.brand=entity.getProduct().getBrand();
		this.name=entity.getProduct().getName();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.updateTime = entity.getUpdateTime().format(formatter);
		this.pImage=entity.getProduct().getImg();
		this.totalPriceString=new DecimalFormat("#,###").format(entity.getTotalPrice());
		this.osId=entity.getId();
		this.optionSet = entity.getOptions().stream().map(t -> new OptionDto.OptionBasicDto(t)).collect(Collectors.toList());
		this.isInterested=entity.getInterests().stream().anyMatch(t -> t.getMember().getUserName().equals(username));
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
