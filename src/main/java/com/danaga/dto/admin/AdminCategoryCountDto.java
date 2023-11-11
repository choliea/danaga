package com.danaga.dto.admin;

import com.danaga.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class AdminCategoryCountDto {
	private Category category;
	private Integer count;

}
