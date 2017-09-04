package com.engage.model;

import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BlocksTest {

	/**
	 * For creating a static instance of validator
	 */
	private static Validator validator;
	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(BlocksTest.class);

	@Before
	public void init() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	/**
	 * Special chars not allowed in Block name
	 */
	@Test
	public void specialCharsInName() {
		Blocks blocks = new Blocks();
		blocks.setBlockName("@##a");

		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "blockName");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}

	/**
	 * Special chars not allowed in Block name
	 */
	@Test
	public void punctuationInBody() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("Abcd.,?!:;'\"");

		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "bodyOfMessage");
		Assert.assertTrue(errors.size() == 0);

	}

	/**
	 * Blank Message
	 */
	@Test
	public void blankBody() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("");

		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "bodyOfMessage");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}

	/**
	 * Special chars in body
	 */
	@Test
	public void specialCharBody() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("@qww1");

		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "bodyOfMessage");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}
	
	/**
	 * More than 500 alphanumeric in body
	 */
	@Test
	public void gt500charsBody() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage(RandomStringUtils.randomAlphanumeric(501));

		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "bodyOfMessage");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}
	
	@Test
	public void noEventId() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("fd");

		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "eventId");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}
	/**
	 * Greater than integer max
	 */
	@Test
	public void largeEventId() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("fd");
		Long l = (Integer.MAX_VALUE)+100L;
		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "eventId");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}

	/**
	 * Negative Pathway id 
	 */
	@Test
	public void negativePathwayId() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("fd");
		Long l = -(Integer.MAX_VALUE)-100L;
		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "pathwayId");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}
	@Test
	public void noPathwayId() {
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("fd");

		Set<ConstraintViolation<Blocks>> errors = validator.validateProperty(
				blocks, "pathwayId");
		logger.info(errors.toString());

		Assert.assertTrue(errors.size() == 1);

	}

}
