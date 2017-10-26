/**
 * 
 */
package com.engage.commons.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;

/**
 * <blockquote>
 * Email validator uses Regex pattern to validate the string.
 *</blockquote>
 * @author mindtech-labs
 *
 */
public final class EmailValidator implements Validator{

	@Override
	public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value,
			Class<?>... groups) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutableValidator forExecutables() {
		// TODO Auto-generated method stub
		return null;
	}

}
