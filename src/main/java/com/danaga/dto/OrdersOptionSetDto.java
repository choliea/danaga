package com.danaga.dto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;

import com.danaga.entity.Orders;

import jakarta.persistence.criteria.Order;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrdersOptionSetDto {
	private String userName;
	private Integer orderItem_qty;
	private String delivaryName;
	private String delivaryPhoneNumber;
	private String delivaryAddress;
	
	public static OrdersOptionSetDto ordersOptionSetDto(Orders entity) {
		
		return OrdersOptionSetDto.builder()
						.userName(entity.getMember().getUserName())
						.orderItem_qty(entity.getOrderItems().get(0).getQty())
						.delivaryName(entity.getDelivery().getName())
						.delivaryPhoneNumber(entity.getDelivery().getPhoneNumber())
						.delivaryAddress(entity.getDelivery().getAddress())
						.build();
	}
	
	
	
}
