package com.danaga.exception;

public class MemberNotFoundException extends Exception {
	private Object data;
	public MemberNotFoundException(String msg) {
		super(msg);
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
