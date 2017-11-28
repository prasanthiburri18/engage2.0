package com.engage.commons.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

import com.engage.commons.validators.annotations.ValidName;
/**
 * This is deprecated. Javax {@link Pattern} is used instead.
 * @author mindtechlabs
 *
 */
public class IsValidName implements ConstraintValidator<ValidName, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
	
		return false;
	}

}
