/**
 * 
 */
package com.engage.commons.exception;

/**
 * This exception should be thrown when there is no patient(s) found against given valid input
 * Eg: Used user tries to fetch PatientList but no patient(s) are created yet.
 * @author mindtechlabs
 *
 */
public class PatientNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1649776731429218165L;

	public PatientNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

	public PatientNotFoundException(String message) {
		super(message);
		
	}

	public PatientNotFoundException(Throwable cause) {
		super(cause);
		
	}

}
