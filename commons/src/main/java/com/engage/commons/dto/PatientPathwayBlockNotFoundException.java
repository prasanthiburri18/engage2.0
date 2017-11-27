package com.engage.commons.dto;
/**
 * This is used when {@link PathwayPatientBlockDto} is null,
 * or List of {@link PathwayPatientBlockDto} returns empty List
 * @author mindtechlabs
 *
 */
public class PatientPathwayBlockNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2344373503929542870L;

	public PatientPathwayBlockNotFoundException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public PatientPathwayBlockNotFoundException(String message) {
		super(message);
	
	}

	public PatientPathwayBlockNotFoundException(Throwable cause) {
		super(cause);
	
	}

}
