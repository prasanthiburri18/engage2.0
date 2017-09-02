package com.engage.commons.validators.utils;

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
	public static <T> Set<String> getArrayOfValidations(Set<ConstraintViolation<T>> errors){
		
		logger.info("Parsing constraint violations");
		Set<String> errormessages = errors.stream().map(constraintViolation -> String.format("{\"%s\":\"%s\"}", constraintViolation.getPropertyPath(),
	               constraintViolation.getMessage()))
	        .collect(Collectors.toSet());
	
		return errormessages;
	}
}
