package com.danaga.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.ManyToAny;

import org.hibernate.annotations.GenericGenerator;

import org.hibernate.annotations.UpdateTimestamp;

import com.danaga.config.OrderStateMsg;
import com.danaga.dto.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.Order;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Orders {
	
	@Id
	@SequenceGenerator(name = "order_order_no_seq",sequenceName = "order_order_no_seq",initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_order_no_seq")
	@Column(length = 20)
	private Long id; 

	@Column(length = 100)
	private String description; //주문설명
	@Column(length = 10)
	private Integer price; //총주문가격
	@Column(length = 100 )
	@Enumerated(EnumType.STRING)
	private OrderStateMsg statement; //주문상태 //notnull
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createDate; //주문날짜

	@EqualsAndHashCode.Exclude
	@OneToOne(mappedBy = "orders", cascade = CascadeType.PERSIST)
	private Delivery delivery; //배송
	
	@OneToOne(mappedBy = "orders")
	private Refund refund; //환불
	
	@OneToMany(mappedBy = "orders",cascade = CascadeType.PERSIST)
	@Builder.Default
	private List<OrderItem> orderItems = new ArrayList<>(); //주문상품목록
	
	@ManyToOne
	@JoinColumn(name = "memberId")
	@ToString.Exclude
	private Member member; //주문자
	
	public static Orders toResponseEntity(OrdersDto ordersDto) {
		return Orders.builder()
					 .createDate(ordersDto.getCreateDate())
					 .description(ordersDto.getDescription())
					 .price(ordersDto.getPrice())
					 .statement(ordersDto.getStateMsg())
					 .build();
		
	}
}
