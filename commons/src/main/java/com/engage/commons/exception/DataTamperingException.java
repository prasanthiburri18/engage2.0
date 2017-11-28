package com.engage.commons.exception;

/**
 * This is to be thrown and handled when there is attempt to modify a resource
 * illegally.
 * 
 * @author mindtechlabs
 *
 */
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
