package com.danaga.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "product")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {//제품의 기본 모델 정보
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;//pk
	private String name;//제품명
	private String brand;//브랜드
	private Integer price;//기본 가격
	private String descImage;//설명 이미지 파일
	private String prevImage;//디테일이미지
	private String img;//제품 이미지
	
	@OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
	@Builder.Default
	private List<CategorySet> categorySets = new ArrayList<>();
	//하나의 제품은 부모카테고리, 자식카테고리 여러개를 가질 수 있다. 
	//예를 들어, 컴퓨터, 일체형PC, 브랜드PC ...
	
	@ToString.Exclude
	@OneToMany(mappedBy = "product",cascade = {CascadeType.REMOVE,CascadeType.PERSIST},orphanRemoval = true)
	@Builder.Default
	private List<OptionSet> optionSets = new ArrayList<>();
	
}
