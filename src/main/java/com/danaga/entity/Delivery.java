package com.danaga.entity;

import java.time.*;



import com.danaga.dto.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class Delivery {
	@Id
	@SequenceGenerator(name = "DELIVERY_DELIVERY_NO_SEQ", sequenceName = "DELIVERY_DELIVERY_NO_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DELIVERY_DELIVERY_NO_SEQ")
	@Column(length = 20)
	private Long id; //PK
	@Column(length = 20)
	private String name; //수령인
	@Column(length = 20)
	private String phoneNumber; //수령인전화번호
	@Column(length = 100)
	private String address; //수령인 주소
	@Column(length = 100)
	private String detailAddress; //수령인 상세주소
	@Column(length = 100)
	private String postCode; //수령인 우편번호
	
	@OneToOne
	@JoinColumn(name = "orderId")
	@ToString.Exclude
	private Orders orders; //주문번호
	 

	
    public static Delivery toEntity(DeliveryDto dto) {
    	return Delivery.builder()
    			.name(dto.getName())
    			.phoneNumber(dto.getPhoneNumber())
    			.address(dto.getAddress())
    			.build(); 
    	
    	
    }
}
