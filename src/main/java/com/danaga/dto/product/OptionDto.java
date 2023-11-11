package com.danaga.dto.product;

import java.util.ArrayList;
import java.util.List;

import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;

import jakarta.validation.constraints.Min;
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
public class OptionDto {
	
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String value;
	@Min(0)
	private Integer extraPrice;
	@NotNull
	private Long optionSetId;

	public Options toEntity() {
		return Options.builder()
				.extraPrice(extraPrice)
				.id(id)
				.name(name)
				.optionSet(OptionSet.builder().id(optionSetId).build())
				.value(value)
				.build();
	}
	public OptionSaveDto toSaveDto() {
		return OptionSaveDto.builder()
				.extraPrice(extraPrice)
				.name(name)
				.optionSetId(optionSetId)
				.value(value)
				.build();
	}
	public OptionDto(Options entity) {
		this.id=entity.getId();
		this.name=entity.getName();
		this.value=entity.getValue();
		this.extraPrice=entity.getExtraPrice();
		this.optionSetId=entity.getOptionSet().getId();
	}
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OptionBasicDto {
		@NotBlank
		private String name;
		@NotBlank
		private String value;
		
		public OptionBasicDto(Options t) {
			this.name=t.getName();
			this.value=t.getValue();
		}
	}
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OptionNameValueDto {
		private List<String> optionNames;
		private List<String> optionValues;
	}
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OptionNameValueMapDto {
		private String optionName;
		@Builder.Default
		private List<String> optionValue=new ArrayList<String>();
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OptionSaveDto {
		@NotNull
		@NotBlank
		private String name;
		@NotNull
		@NotBlank
		private String value;
		private Integer extraPrice;
		@NotNull
		private Long optionSetId;
		
		public Options toEntity() {
			return Options.builder()
					.name(name)
					.value(value)
					.optionSet(OptionSet.builder().id(optionSetId).build())
					.extraPrice(extraPrice)
					.build();
		}
	}
}
