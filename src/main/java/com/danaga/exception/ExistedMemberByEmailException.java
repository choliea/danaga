package com.danaga.exception;

public class ExistedMemberByEmailException extends Exception{
	private Object data;
	public ExistedMemberByEmailException(String msg) {
		super(msg);
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
