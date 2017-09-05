/**
 * 
 */
package com.engage.model;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Email of Patient is not present on client side. 
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
public class ValidatePathwayTest {
	
	/**
	 * For creating a static instance of validator
	 */
	private static Validator validator;
	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ValidatePathwayTest.class);

	/***
	 * Initialize validator
	 */
	@Before
	public void init() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	/**
	 * Corrrect patient format - positive test
	 */
	@Test
	public void correctPathwayNameFormat(){
		Pathway pathway = new Pathway();
		pathway.setClinicianId(1);
		pathway.setPathwayName("2adhlAXWld1 ");
		Set<ConstraintViolation<Pathway>> errors = validator.validate(pathway);
		
		org.junit.Assert.assertEquals(errors.size(), 0);
		}
	

	@Test
	public void nullPathwayFormat(){
		Pathway pathway = new Pathway();
		pathway.setClinicianId(1);
		pathway.setPathwayName(null);
		Set<ConstraintViolation<Pathway>> errors = validator.validate(pathway);
		
		org.junit.Assert.assertEquals(errors.size(), 2);
		}


	@Test
	public void blankPathwayFormat(){
		Pathway pathway = new Pathway();
		pathway.setClinicianId(1);
		pathway.setPathwayName("");
		Set<ConstraintViolation<Pathway>> errors = validator.validate(pathway);
		
		org.junit.Assert.assertEquals(errors.size(), 1);
		}
	
	@Test
	public void whitespacePathwayFormat(){
		Pathway pathway = new Pathway();
		pathway.setClinicianId(1);
		pathway.setPathwayName("     ");
		Set<ConstraintViolation<Pathway>> errors = validator.validate(pathway);
		
		org.junit.Assert.assertEquals(errors.size(), 1);
		}
	
	
	@Test
	public void outputJson() throws JsonProcessingException{
	
		PatientPathway patientPathway = new PatientPathway();
		
		
		ObjectMapper mapper = new ObjectMapper();

		logger.info(mapper.writeValueAsString(patientPathway));
		}
	
	
}
