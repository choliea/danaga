package com.danaga.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.danaga.config.OrderStateMsg;
import com.danaga.dto.product.OptionDto;
import com.danaga.entity.OrderItem;
import com.danaga.entity.Orders;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrdersDto {
	
	private Long id;
	private String description;
	private Integer price;
	private OrderStateMsg stateMsg;
	private LocalDateTime createDate;
	private String userName;
	@Builder.Default
	private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
	
	public static OrdersDto orderDto(Orders entity) {
		return OrdersDto.builder()
						.id(entity.getId())
						.createDate(entity.getCreateDate())
						.description(entity.getDescription())
						.price(entity.getPrice())
						.stateMsg(entity.getStatement())
						.userName(entity.getMember().getUserName())
						.orderItemDtoList(entity.getOrderItems().stream().map(t -> new OrderItemDto(t)).collect(Collectors.toList()))
						.build();
	}
	
	
	
}
