package com.danaga.entity;

import org.hibernate.annotations.DialectOverride.ColumnDefault;

import jakarta.persistence.Column;
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

@Builder
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recent_view", uniqueConstraints = @UniqueConstraint(columnNames = {"memberId","optionSetId"}))
public class RecentView extends BaseEntity{//멤버아이디와 프로덕트아이디로 고유키 제약 
	//최근 조회한 상품 
	//30일간만 보관 
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "memberId")
	@ToString.Exclude
	private Member member;//member FK
	@ManyToOne
	@JoinColumn(name = "optionSetId")
	private OptionSet optionSet;//optionSet FK
}
