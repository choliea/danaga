package com.danaga.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Options {//옵션셋FK를 가지는 오너테이블
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;//pk
	
	private String name; //옵션명
	private String value; //옵션값
	private Integer extraPrice;
	//해당 옵션이 옵션셋에 등록될 경우 프로덕트의 총 가격에 추가금
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "optionSetId")
	@ToString.Exclude
	private OptionSet optionSet;//옵션셋 FK
}
