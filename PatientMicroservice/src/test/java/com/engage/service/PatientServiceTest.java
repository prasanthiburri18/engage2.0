/**
 * 
 */
package com.engage.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.dto.PatientListDto;
import com.engage.exception.PatientNotFoundException;

/**
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(PatientServiceTest.class);
	@Autowired
	private PatientService patientService;
	
	@Test
	public void patientListTest() throws PatientNotFoundException{
		Long orgId = new Long(285);
		List<PatientListDto> pld=	patientService.getPatientList(orgId);
		logger.info(pld.toString());
	}
}
