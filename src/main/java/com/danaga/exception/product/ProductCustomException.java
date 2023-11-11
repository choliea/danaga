package com.danaga.exception.product;

import lombok.Data;

@Data
public class ProductCustomException extends Exception{
	private String data;
	private ProductExceptionMsg msg;
	
	public ProductCustomException(ProductExceptionMsg msg) {
		this.msg = msg;
	}

	public ProductCustomException(String data, ProductExceptionMsg msg) {
		this.data = data;
		this.msg = msg;
	}
}
