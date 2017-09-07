package com.engage.model;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Ignore
public class EventsTest {

	

	/**
	 * For creating a static instance of validator
	 */
	private static Validator validator;
	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(EventsTest.class);

	@Before
	public void init() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

}
