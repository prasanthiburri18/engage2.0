package com.engage.commons.exception;

public class InvalidAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6887650004921383800L;

	/**
	 * @param message
	 */
	public InvalidAccessException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public InvalidAccessException(Throwable cause) {
		super(cause);

	}

}
