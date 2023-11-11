package com.danaga.dto;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.danaga.dto.product.OptionDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.entity.OptionSet;
import com.danaga.entity.OrderItem;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderItemDto {
	private Integer qty;
	private Long orderId;
	private String productName;
	private String pImage;
	private Integer totalPrice;
	private Long osId;
	@Builder.Default
	private List<OptionDto> optionSet = new ArrayList<>();
	
	public OrderItemDto(OrderItem entity) {
		this.qty=entity.getQty();
		this.orderId=entity.getOrders().getId();
		this.productName=entity.getOptionSet().getProduct().getName();
		this.pImage=entity.getOptionSet().getProduct().getImg();
		this.totalPrice=entity.getOptionSet().getTotalPrice();
		this.osId = entity.getOptionSet().getId();
		this.optionSet = entity.getOptionSet().getOptions().stream().map(t -> new OptionDto(t)).collect(Collectors.toList());
	}
}
