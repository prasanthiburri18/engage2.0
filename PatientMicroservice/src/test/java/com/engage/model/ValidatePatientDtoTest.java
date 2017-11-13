/**
 * 
 */
package com.engage.model;

import java.sql.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.commons.validators.utils.ConstraintValidationUtils;
import com.engage.dto.PatientDto;

/**
 * Email of Patient is not present on client side. 
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
//@Ignore
public class ValidatePatientDtoTest {
	
	/**
	 * For creating a static instance of validator
	 */
	private static Validator validator;
	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(ValidatePatientDtoTest.class);

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
	public void correctPatientFormat(){
		PatientDto p = new PatientDto();
		p.setClinicianId(1);
		p.setFirstName("afffaf");
		p.setLastName("adfdfd sf");
		p.setorgId(1);
		p.setDob(new Date(System.currentTimeMillis()-100000000));
		p.setPhone("1231231234");
		Set<ConstraintViolation<PatientDto>> errors = validator.validate(p);
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		
		org.junit.Assert.assertEquals(errors.size(), 0);
		}
	

	/**
	 * Blank patient format - positive test
	 */
	@Test
	public void incorrectPatientFormat(){
		PatientDto p = new PatientDto();
		
		Set<ConstraintViolation<PatientDto>> errors = validator.validateProperty(p,"firstName");
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		org.junit.Assert.assertEquals(errors.size(), 2);
		}
	

	/**
	 * Special chars - negative test
	 */
	@Test
	public void specialCharsInFirstName(){
		PatientDto p = new PatientDto();
		p.setFirstName("dlfdl R");
		p.setLastName("dlfdl^ _R");
		Set<ConstraintViolation<PatientDto>> errors = validator.validateProperty(p,"lastName");
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		org.junit.Assert.assertEquals(errors.size(), 1);
		}
	
	/**
	 * Greater than 60 chars last name - negative test
	 */
	@Test
	public void gt60CharsInLastName(){
		PatientDto p = new PatientDto();
		p.setLastName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijk");;
		Set<ConstraintViolation<PatientDto>> errors = validator.validateProperty(p,"lastName");
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		org.junit.Assert.assertEquals(errors.size(), 1);
		}
	
	/**
	 * Less than 1 chars last name - negative test
	 */
	@Test
	public void lt30CharsInFirstName(){
		PatientDto p = new PatientDto();
		p.setLastName("");;
		Set<ConstraintViolation<PatientDto>> errors = validator.validateProperty(p,"firstName");
		logger.info(ConstraintValidationUtils.getArrayOfValidations(errors).toString());
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		org.junit.Assert.assertEquals(errors.size(), 2);
		}
	
	/**
	 * Invalid organization id - negative test
	 */
	@Test
	public void invalidOrgid(){
		PatientDto p = new PatientDto();
		p.setorgId(0);
		Set<ConstraintViolation<PatientDto>> errors = validator.validateProperty(p,"orgId");
		logger.info(ConstraintValidationUtils.getArrayOfValidations(errors).toString());
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		org.junit.Assert.assertEquals(errors.size(), 1);
		}
	/**
	 * Blank organization id - negative test
	 */
	@Test
	public void blankOrgid(){
		PatientDto p = new PatientDto();
		//p.setorgId();
		Set<ConstraintViolation<PatientDto>> errors = validator.validateProperty(p,"orgId");
		logger.info(ConstraintValidationUtils.getArrayOfValidations(errors).toString());
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		org.junit.Assert.assertEquals(errors.size(), 1);
		}
	
	@Test
	public void dobWithTodayTest(){
		PatientDto p = new PatientDto();
		p.setDob(new Date(System.currentTimeMillis()));
		//p.setorgId();
		Set<ConstraintViolation<PatientDto>> errors = validator.validateProperty(p,"dob");
		logger.info(ConstraintValidationUtils.getArrayOfValidations(errors).toString());
		logger.info(ConstraintValidationUtils.getMapOfValidations(errors).toString());
		org.junit.Assert.assertEquals(errors.size(), 0);
	}
}
