package com.engage.commons.exception;

public class ConstraintViolationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3243035069591043489L;

	public ConstraintViolationException(String arg0) {
		super(arg0);
		
	}

	public ConstraintViolationException(Throwable arg0) {
		super(arg0);
		
	}

}
