package com.danaga.exception;

public class ExistedMemberByUserNameException extends Exception{
	private Object data;
	public ExistedMemberByUserNameException(String msg) {
		super(msg);
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
