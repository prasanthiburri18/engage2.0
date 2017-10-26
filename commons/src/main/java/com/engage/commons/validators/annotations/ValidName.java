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

import com.engage.commons.validators.IsValidName;

/**
 * @author ai
 *
 */

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy={IsValidName.class})
public @interface ValidName {

	int min() default 0;
	int max() default 500;
	
	String message() default "Invalid format";
	Class<?>[] groups() default{};
	Class<? extends Payload>[] payload() default { };
	
	
}
