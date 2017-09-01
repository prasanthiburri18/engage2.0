/**
 * 
 */
package com.engage.commons.validators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.engage.commons.validators.annotations.ValidPhoneNumber;

/**
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
		Pattern pattern = Pattern.compile("^[1-9]\\d{9}");
		if (value.matches(pattern.toString())) {
			return true;
		}

		return false;
	}

}
