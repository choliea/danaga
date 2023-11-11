package com.danaga.dto.product;

import com.danaga.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	
	@NotNull
	private Long id;
	@NotBlank
	private String name;
	private Long parentId;
	
	public Category toEntity() {
		return Category.builder()
				.id(id)
				.name(name)
				.parent(Category.builder().id(getParentId()).build())
				.build();
	}
	
	public CategoryDto(Category entity){
		this.id=entity.getId();
		this.name=entity.getName();
		if(entity.getParent()!=null&&entity.getParent().getId()!=null) {
			this.parentId=entity.getParent().getId();
		}
	}
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class CategorySaveDto {
		@NotBlank
		private String name;
		private Long parentId;
		
		public Category toEntity() {
			return Category.builder()
					.name(name)
					.parent(Category.builder().id(parentId).build())
					.build();
		}
		CategorySaveDto(Category entity){
			this.name=entity.getName();
			this.parentId=entity.getParent().getId();
		}
	}
}
