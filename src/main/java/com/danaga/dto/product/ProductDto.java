package com.danaga.dto.product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class ProductDto {//상세페이지
	private String brand;
	private String name;
	private String updateTime;
	private String descImage;
	private String prevImage;
	private String pImage;
//	@Builder.Default
//	private List<Category> categorySet=new ArrayList<>();
	private Integer stock;
	private Integer totalPrice;
	private Long osId;
	@Builder.Default
	private List<OptionDto> optionSet = new ArrayList<>();
	private Boolean isInterested;
	
	
	public ProductDto(OptionSet entity) {
		this.brand=entity.getProduct().getBrand();
		this.name=entity.getProduct().getName();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.updateTime = entity.getUpdateTime().format(formatter);
		this.descImage=entity.getProduct().getDescImage();
		this.prevImage=entity.getProduct().getPrevImage();
		this.pImage=entity.getProduct().getImg();
		this.stock=entity.getStock();
		this.totalPrice=entity.getTotalPrice();
		this.osId=entity.getId();
		this.optionSet = entity.getOptions().stream().map(t -> new OptionDto(t)).collect(Collectors.toList());
		this.isInterested=false;
	}
	public ProductDto(OptionSet entity, String username) {
		this.brand=entity.getProduct().getBrand();
		this.name=entity.getProduct().getName();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.updateTime = entity.getUpdateTime().format(formatter);
		this.descImage=entity.getProduct().getDescImage();
		this.prevImage=entity.getProduct().getPrevImage();
		this.pImage=entity.getProduct().getImg();
		this.stock=entity.getStock();
		this.totalPrice=entity.getTotalPrice();
		this.osId=entity.getId();
		this.optionSet = entity.getOptions().stream().map(t -> new OptionDto(t)).collect(Collectors.toList());
		this.isInterested=entity.getInterests().stream().anyMatch(t -> t.getMember().getUserName().equals(username));
	}
}
