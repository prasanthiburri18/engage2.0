package com.engage.commons.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.engage.commons.validators.annotations.ValidName;

public class IsValidName implements ConstraintValidator<ValidName, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initialize(ValidName constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

}
