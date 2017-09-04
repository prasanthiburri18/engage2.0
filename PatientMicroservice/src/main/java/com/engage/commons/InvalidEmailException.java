package com.engage.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidEmailException extends ConstraintViolationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3803711011911557084L;
	private static final Logger logger = LoggerFactory.getLogger(InvalidEmailException.class);
	public InvalidEmailException(String arg0) {
		super(arg0);
		logger.warn(arg0);
	}

	public InvalidEmailException(Throwable arg0) {
		super(arg0);
		logger.warn(arg0.getMessage());
	}

}
