/**
 * 
 */
package com.engage.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.commons.exception.MessageUpdateFailedException;

/**
 * @author mindtechlabs
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class SchedulerServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerServiceTest.class);
	@Autowired
	private SchedulerService schedulerService;
	@Test
	@Ignore
	public void getPatientByIdTest(){
		
		Integer patientId = 1000000;
		String ph = schedulerService.patientPhoneById(patientId);
		LOGGER.info(ph);
	}
	
	@Test
	@Ignore
	public void updatePatientmessagestatusTest() throws MessageUpdateFailedException{
		
		Integer recordId = 3;
		Integer status = schedulerService.updatePatientmessagestatus(recordId);
		
		LOGGER.info("Status: " +status);
	}
	
	@Test
	@Ignore
	public void dayTwoCronServiceTest(){
		

		List<Object> cronList = schedulerService.getBlockcrondataexecute();
		
		LOGGER.info("Status: " +cronList.size());
	}
	
	
	@Test
	public void dayOneCronServiceTest(){
		

		List<Object> cronList = schedulerService.getFirstdayBlockcrondataexecute();
		
		LOGGER.info("Status: " +cronList.size());
	}
}
