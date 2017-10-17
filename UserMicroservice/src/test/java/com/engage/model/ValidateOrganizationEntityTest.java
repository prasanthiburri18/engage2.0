/**
 * 
 */
package com.engage.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
@Ignore

public class ValidateOrganizationEntityTest {
	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ValidateOrganizationEntityTest.class);
	/**
	 * Static validator to be used across the UnitTest
	 */
	private static Validator validator;

	@Before
	public void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	/**
	 * Org name with special chars - negative test
	 */
	@Test
	public void specialCharOrganizationNameFormat() {
		Organization org = new Organization();
		org.setName("pammd$$d*");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		Assert.assertTrue(violations.size() == 1);
		Assert.assertEquals(violations.stream().findFirst().get().getMessage(),
				"Only alphabetic characters are allowed.");
	}
	/**
	 * Name with spaces - positive test
	 */
	@Test
	public void correctOrganizationNameWithSpacesFormat() {
		Organization org = new Organization();
		org.setName("pammd kldfdll");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		Assert.assertTrue(violations.isEmpty());
	}
	/**
	 * Empty string is passed - negative test
	 */
	@Test
	public void nullOrganizationNameFormat() {
		Organization org = new Organization();
		org.setName(null);
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		violations.stream().forEach(c -> logger.info(c.getMessage()));
		Assert.assertTrue(violations.size()==2);
		//Assert.assertEquals(violations.stream().findFirst().get().getMessage(),"Organization name cannot be empty." );

	}
	/**
	 * Empty string is passed - negative test
	 */
	@Test
	public void blankOrganizationNameFormat() {
		Organization org = new Organization();
		org.setName("");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		Assert.assertTrue(violations.size()>0);
	//	Assert.assertEquals(violations.stream().findFirst().get().getMessage(),"Organization name should be a minimum of 2 characters." );

	}

	/**
	 * More than 2 empty spaces (blank)
	 */
	@Test
	public void whitespacesOrganizationNameFormat() {
		Organization org = new Organization();
		org.setName("   ");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		Assert.assertTrue(violations.size() == 1);

		Assert.assertEquals(violations.stream().findFirst().get().getMessage(), "Organization name cannot be blank.");

	}

	/**
	 * Just one empty spaces (blank)
	 * Expect for min and blank error
	 */
	@Test
	public void whitespacesOrganizationNameFormat2() {
		Organization org = new Organization();
		org.setName(" ");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		violations.stream().forEach(c -> logger.info(c.getMessage()));
		Assert.assertTrue(violations.size()>0);
	}

	/**
	 * 60 alphabet chars - positive test
	 */
	@Test
	public void maxOrganizationNameFormat() {
		Organization org = new Organization();
		org.setName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghicdefghijk");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		Assert.assertTrue(violations.isEmpty());

	}
	/**
	 * More than 60 alphabetic chars - negative test
	 */
	@Test
	public void maxOrganizationNameFormat2() {
		Organization org = new Organization();
		org.setName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghicadfdfdefghijk");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
		Assert.assertTrue(violations.size()==1);
Assert.assertEquals(violations.iterator().next().getMessage(), "Organization name exceeds 60 characters.");
	}
	
	/**
	 * Invalid pattern, less than 2 char--negative test
	 */
	@Test
	public void oneSpecialCharacter(){
		Organization org = new Organization();
		org.setName("$");
		Set<ConstraintViolation<Organization>> violations = validator.validateProperty(org, "name");
	//	Assert.assertTrue(!violations.isEmpty());
		Assert.assertTrue(violations.size()>0);
	}
}
