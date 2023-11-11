package com.danaga.exception.product;

public class NeedLoginException extends ProductCustomException {

	public NeedLoginException(ProductExceptionMsg msg) {
		super(msg);
	}
	public NeedLoginException(String data,ProductExceptionMsg msg) {
		super(data,msg);
	}
	public NeedLoginException() {
		super(ProductExceptionMsg.NEED_LOGIN);
	}
	public NeedLoginException(String data) {
		super(data,ProductExceptionMsg.NEED_LOGIN);
	}

}
