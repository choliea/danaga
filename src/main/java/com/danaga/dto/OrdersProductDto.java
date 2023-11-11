package com.danaga.dto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;

import com.danaga.entity.Delivery;
import com.danaga.entity.Orders;

import jakarta.persistence.criteria.Order;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrdersProductDto {
	private Long optionSetId;
	private Integer orderItem_qty;
	private String delivaryName;
	private String delivaryPhoneNumber;
	private String delivaryAddress;
	private String delivaryDetailAddress;
	private String deliveryPostCode;

	
	public static OrdersProductDto OrdersResponseDto(Orders entity) {
		
		return OrdersProductDto.builder()
						.optionSetId(entity.getOrderItems().get(0).getOptionSet().getId())
						.orderItem_qty(entity.getOrderItems().get(0).getQty())
						.build();
	}
	
	
}
