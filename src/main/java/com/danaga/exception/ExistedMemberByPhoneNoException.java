package com.danaga.exception;

public class ExistedMemberByPhoneNoException extends Exception{
	private Object data;
	public ExistedMemberByPhoneNoException(String msg) {
		super(msg);
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
