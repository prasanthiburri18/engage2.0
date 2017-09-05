package com.engage.commons;

import java.util.Map;

import org.json.JSONObject;

public class ConstraintViolationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3243035069591043489L;
	
	private JSONObject jsonObject;
	
	public ConstraintViolationException(String arg0) {
		super(arg0);
		
	}

	public ConstraintViolationException(Throwable arg0) {
		super(arg0);
		
	}
	public ConstraintViolationException(Map<String, String> arg0) {
		
		this.setJsonObject(new JSONObject(arg0));
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
}
