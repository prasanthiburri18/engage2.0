/**
 * 
 */
package com.engage.commons.validators.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.engage.commons.validators.IsValidPhoneNumberFormat;

/**
 * @author mindtech-labs
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy={IsValidPhoneNumberFormat.class})
public @interface ValidPhoneNumber {

	String message() default "Invalid phone number format";
	Class<?>[] groups() default{};
	Class<? extends Payload>[] payload() default { };

}
