package com.danaga.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
	@CreationTimestamp
	@Column(updatable = false)
	@ToString.Exclude
	private LocalDateTime createTime;//데이터 생성시간
	@UpdateTimestamp
	private LocalDateTime updateTime;//데이터 갱신시간
}
