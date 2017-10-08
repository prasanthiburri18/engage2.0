package com.engage.commons.exception;

public class DataTamperingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 146416847950064550L;

	public DataTamperingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public DataTamperingException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public DataTamperingException(String message) {
		super(message);

	}

}
