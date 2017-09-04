package com.engage.model;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;

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

import com.engage.commons.validators.utils.ConstraintValidationUtils;

/**
 * Unit Test to validate User entity User id can be null, as hibernate inserts
 * by sequence strategy
 * 
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
public class ValidateUserTest {
	/**
	 * For creating a static instance of validator
	 */
	private static Validator validator;
	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ValidateUserTest.class);

	/***
	 * Initialize validator
	 */
	@Before
	public void init() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	
	/**
	 * Entity validation negative test
	 */
	@Test
	public void checkForAllValidations(){
		User user = new User();
		Set<ConstraintViolation<User>> set = validator.validate(user);		
		logger.info(set.toString());
		org.junit.Assert.assertTrue(!set.isEmpty());
	}
	
	/**
	 * Entity validation negative test
	 * User object cannot be null
	 */
	
	@Test(expected=IllegalArgumentException.class)
	public void passNullUser(){
		User user = null;;
		Set<ConstraintViolation<User>> set = validator.validate(user);		
		logger.info(set.toString());
		org.junit.Assert.assertTrue(!set.isEmpty());
	}
	
	
	/**
	  * Email validation -correct format
	  */
	@Test
	public void validateEmailCorrectFormat(){
		User user = new User();
		user.setEmail("abc@yd");
			
			Set<ConstraintViolation<User>> set = validator.validateProperty(user, "email");		
			org.junit.Assert.assertTrue(set.isEmpty());
	}

	/**
	  * Email validation -incorrect format1
	  */
	@Test
	public void validateEmailIncorrectFormat1() {
		User user = new User();
		user.setEmail("@yd");

		Set<ConstraintViolation<User>> set = validator.validate(user);//, "email");
		
		//assertTrue(set.size()==1);
		//Map<Object, Object> errormessage = set.stream().collect(Collectors.toMap(ConstraintViolation::getConstraintDescriptor, ConstraintViolation::getMessage));
		
		
		Set<String> errormessage = set.stream().map(constraintViolation -> String.format("{\"%s\":\"%s\"}", constraintViolation.getPropertyPath(),
               constraintViolation.getMessage()))
        .collect(Collectors.toSet());
		errormessage = ConstraintValidationUtils.getArrayOfValidations(set);
		logger.info(errormessage.toString());
		Set<String> errormessages = set.stream().map(v->v.getMessage()).collect(Collectors.toSet());
		//errormessages.forEach(c->logger.info(c+"\n"));
		assertEquals(set.iterator().next().getMessage(), "Incorrect email format.");
	}
	
	/**
	  * Email validation -incorrect format2
	  */
	@Test
	public void validateEmailInCorrectFormat2() {
		User user = new User();
		user.setEmail(" @yd");

		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "email");

		org.junit.Assert.assertTrue(!set.isEmpty());
		
	}
	@Test
	public void validateEmailInCorrectFormat3() {
		User user = new User();
		user.setEmail("  adfdf@yd   ");

		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "email");

		org.junit.Assert.assertTrue(!set.isEmpty());
		
	}

	@Test
	public void validateEmailIncorrectFormat_ExpectCorrectMessage() {
		User user = new User();
		user.setEmail("add1233dd");

		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "email");

		org.junit.Assert.assertTrue(!set.isEmpty());
		org.junit.Assert.assertEquals(set.iterator().next().getMessage(), "Incorrect email format.");
	}

	/**
	 * Incorrect email format expect error
	 */
	@Test
	public void validateBlankEmail_ExpectCorrectMessage() {
		User user = new User();
		user.setEmail("");

		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "email");

		org.junit.Assert.assertTrue(!set.isEmpty());
		org.junit.Assert.assertEquals(set.iterator().next().getMessage(), "Incorrect email format.");
	}

	/**
	 * Used alpha numeric
	 */
	@Test
	public void validateIncorrectFormatPhoneNumber1() {
		User user = new User();
		user.setPhone("1ddfddfdf11-9999999");

		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "phone");

		logger.info(set.toString());
		org.junit.Assert.assertTrue("Phone format validated: " + set.iterator().next(), !set.isEmpty());

	}
	/**
	 * Test for ConstraintValidationUtils
	 */
	@Test
	public void validateEmptyUserAndConstraintValidationUtils(){

		User user = new User();
		user.setPhone("1ddfddfdf11-9999999");

		Set<ConstraintViolation<User>> set = validator.validate(user);

		logger.info(ConstraintValidationUtils.getMapOfValidations(set).toString());

	}
	
	/**
	 * Used 10 digits with no hyphens -constraint error
	 */
	@Test
	public void validateIncorrectFormatPhoneNumber2() {
		User user = new User();
		user.setPhone("0234567890");

		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "phone");

		logger.info(set.toString());
		org.junit.Assert.assertTrue("Phone format validated: " + set.iterator().next(), !set.isEmpty());

	}

	/**
	 * Used alpha numerics and find error
	 */
	@Test
	public void validatePhoneNumber_withSpecialChars() {
		User user = new User();
		user.setPhone("select * from users_db where id like '%1'");

		Set<ConstraintViolation<User>> set = validator.validateProperty(user, "phone");

		logger.info(set.toString());
		org.junit.Assert.assertTrue("Phone format validated: " + set.iterator().next(), !set.isEmpty());

	}
	
	/**
	 * Null Practice name validation -negative test
	 */
	@Test
	public void validatePracticeName(){
		User user = new User();
		Set<ConstraintViolation<User>> set = validator.validateProperty(user,"practiceName");		
		logger.info(set.toString());
		Set<String> errormessages = set.stream().map(v->v.getMessage()).collect(Collectors.toSet());
		errormessages.forEach(c->logger.info(c));
		org.junit.Assert.assertTrue(!set.isEmpty());
	}
	/**
	 * Negative test - Full name more than 60 chars
	 */
	@Test
	public void validateUserFullnameMoreThan60Chars(){
		User user = new User();
		user.setFullName("adfdf dfasdfadfdf dfasdfadfdf dfasdfadfdf dfasdfadfdf dfasdfadfdf dfasdf");
		Set<ConstraintViolation<User>> set = validator.validateProperty(user,"fullName");
		assertEquals(set.iterator().next().getMessage(), "Full name exceeds 60 characters.");
	
		
	}
}
