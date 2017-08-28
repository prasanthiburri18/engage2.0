package com.engage.util;

public class JsonMessage {
	private String message;
	private Object data;
	private int statuscode;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public JsonMessage(String message, Object data, int statuscode) {
		super();
		this.message = message;
		this.data = data;
		this.statuscode = statuscode;
	}
	public JsonMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
