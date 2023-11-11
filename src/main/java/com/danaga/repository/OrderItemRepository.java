package com.danaga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.danaga.dto.OrderItemDto;
import com.danaga.entity.OrderItem;
import com.danaga.entity.Orders;

import jakarta.persistence.criteria.Order;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	
	
}
