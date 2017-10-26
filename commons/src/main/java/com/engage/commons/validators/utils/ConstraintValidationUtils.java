package com.engage.commons.validators.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Validation messages Utility class
 * @author mindtech-labs
 *
 */
public class ConstraintValidationUtils {
	/**
	 * Log4j2 wrapper
	 */
	private static Logger logger = LoggerFactory.getLogger(ConstraintValidationUtils.class);
	public synchronized static <T> Set<String> getArrayOfValidations(Set<ConstraintViolation<T>> errors){
		
		logger.info("Parsing constraint violations");
		Set<String> errormessages = errors.stream().map(constraintViolation -> String.format("{\"%s\":\"%s\"}", constraintViolation.getPropertyPath(),
	               constraintViolation.getMessage()))
	        .collect(Collectors.toSet());
	
		return errormessages;
	}
	/**
	 * Returns map when error are passed
	 * @param errors
	 * @return
	 */
public synchronized static <T> Map<String, String> getMapOfValidations(Set<ConstraintViolation<T>> errors){
		
		logger.info("Parsing constraint violations");
		
		Map<String, String> errormessages = new HashMap<>();
		errors.forEach(e->errormessages.put(e.getPropertyPath().toString(),e.getMessage()));
	
		return errormessages;
	}

}
