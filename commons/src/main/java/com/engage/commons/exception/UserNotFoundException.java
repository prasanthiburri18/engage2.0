/**
 * 
 */
package com.engage.commons.exception;

/**
 * @author mindtech-labs
 *
 */
public class UserNotFoundException extends Exception {

	/**
	 * @param message
	 * @param cause
	 */
	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	
	}

	/**
	 * @param message
	 */
	public UserNotFoundException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public UserNotFoundException(Throwable cause) {
		super(cause);

	}


}
