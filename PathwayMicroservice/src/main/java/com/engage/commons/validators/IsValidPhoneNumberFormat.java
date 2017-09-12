/**
 * 
 */
package com.engage.commons.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.engage.commons.validators.annotations.ValidPhoneNumber;

/**
 * If field is null, then default or appended message is returned
 * @author mindtech-labs
 *
 */
public final class IsValidPhoneNumberFormat implements ConstraintValidator<ValidPhoneNumber, String> {
	/**
	 * Phone number should
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		//Removed this as hyphens are not stored db and phone number should not start with 0
		//Pattern pattern = Pattern.compile("\\d{3}[-]\\d{3}[-]\\d{4}");
		if(value==null || value==""||value.trim()==""){
			return true;
		}
		else if (value.matches(("^[1-9]\\d{9}"))) {
			return true;
		}

		return false;
	}

	@Override
	public void initialize(ValidPhoneNumber constraintAnnotation) {
	}

}
