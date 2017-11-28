package com.engage.commons.exception;

import java.util.Map;

import org.json.JSONObject;
/**
 * Common exception used across the application to check server side validation.
 * @author mindtechlabs
 *
 */
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
