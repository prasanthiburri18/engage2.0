package com.engage.commons.exception;

/**
 * This is thrown when a user who doesn't have the privilege(admin role here),
 * yet attempts to access a admin only resource
 * 
 * @author mindtechlabs
 *
 */
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
