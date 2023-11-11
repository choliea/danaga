package com.danaga.dto;

import java.util.List;

import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.ProductMsgInterface;
import com.danaga.exception.product.ProductSuccessMsg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto<T> { //HTTP 응답으로 사용할 DTO 
	private ProductMsgInterface msg;
	private List<T> data;//다른 모델의 DTO도 담을 수 있게 제네릭
	//보통 여러개의 데이터를 리스트에 담아 처리하기 때문에 리스트 
}
