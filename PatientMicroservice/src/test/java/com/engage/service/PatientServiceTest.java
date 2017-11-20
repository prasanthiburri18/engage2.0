/**
 * 
 */
package com.engage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
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
	@Ignore
	public void patientListTest() throws PatientNotFoundException{
		Long orgId = new Long(285);
		Map<Long,PatientListDto> pld=	patientService.getPatientList(orgId);
		
		for(Long l : pld.keySet()){
			
			logger.info(pld.get(l).toString());
		}
		logger.info(pld.toString());
	}
	
	@Test
	@Ignore
	public void patientListTestSomePatientWithoutPathway() throws PatientNotFoundException{
		Long orgId = new Long(274);
		Map<Long,PatientListDto> pld=	patientService.getPatientList(orgId);
		
		for(Long l : pld.keySet()){
			
			logger.info(pld.get(l).toString());
		}
		logger.info(pld.toString());
	}
	
	@Test
	public void getFirstMessageOfPathwayTest(){
		ArrayList messageList = patientService.getPathwayFirstMessageforpatient(1);
		logger.info("Size: "+messageList.size());
		
	}
	
}
