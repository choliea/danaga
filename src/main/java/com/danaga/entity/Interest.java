package com.danaga.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "interest", uniqueConstraints = @UniqueConstraint(columnNames = {"memberId","optionSetId"}))
public class Interest extends BaseEntity{//관심상품
//유저와 옵션셋을 이어주는 중간테이블
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;//pk
	
	@JoinColumn(name = "memberId")
	@ManyToOne
	private Member member;// 유저FK
	
	@JoinColumn(name = "optionSetId")
	@ManyToOne
	@ToString.Exclude
	private OptionSet optionSet;// 옵션셋FK
	
}
