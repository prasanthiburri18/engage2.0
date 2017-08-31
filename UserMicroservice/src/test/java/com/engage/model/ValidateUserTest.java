package com.engage.model;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.Assert;
/**
 * Unit Test to validate User entity
 * User id can be null, as hibernate inserts by sequence strategy
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
public class ValidateUserTest {
	private static Validator validator;
	private static Logger logger = LoggerFactory.getLogger(ValidateUserTest.class);
	 @Before
	    public void setUp() {
	        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	        validator = factory.getValidator();
	    }
	@Test
	public void validateBlankEmail_ExpectCorrectMessage(){
		User user = new User();
		user.setCreateDate(new Date());
		user.setEmail("");
		user.setFullName("");
		user.setPhone("kkkhkk");
		user.setPracticeName(null);
		
		
		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "email");	
		
		
		org.junit.Assert.assertTrue("Blank email validated: "+set.iterator().next(), !set.isEmpty());
	
	}
}
