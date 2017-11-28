package com.engage.commons.exception;
/**
 * This exception should be thrown when there is no pathway(s) found against given valid input
 * Eg: Used user tries to fetch PathwayList but no pathway(s) are created yet.
 * @author mindtechlabs
 *
 */
public class PathwayNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1531481171494152587L;

	public PathwayNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public PathwayNotFoundException(String message) {
		super(message);
		
	}

	public PathwayNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
