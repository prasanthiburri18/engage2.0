/**
 * 
 */
package com.engage.commons.exception;

/**
 * @author mindtechlabs
 *
 */
public class MessageUpdateFailedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2401328887895556051L;

	public MessageUpdateFailedException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public MessageUpdateFailedException(String message) {
		super(message);
	
	}

	public MessageUpdateFailedException(Throwable cause) {
		super(cause);
		
	}

	
}
