package com.danaga.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CategorySet {//중간테이블, 오너테이블
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE) 
	private Long id; //pk
	
	@JoinColumn(name="categoryId")
	@ManyToOne
	private Category category; //카테고리 FK
	
	@JoinColumn(name = "productId")
	@ManyToOne
	@ToString.Exclude
	private Product product; //프로덕트 FK
}
