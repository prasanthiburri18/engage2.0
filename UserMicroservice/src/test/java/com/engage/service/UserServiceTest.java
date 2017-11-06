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

import com.engage.model.User;
import com.engage.util.JsonMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore

public class UserServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
	@Autowired
	private UserService userService;

	@Test
	@Ignore
	public void getPatientByDobTest(){
		userService.verifyPatientInfobydob("2000-02-01");
		LOGGER.info("Test passed");
	}
	@Test
	@Ignore
	public void getPatientsByPhone(){
		List<Object> patients = userService.patientidbyphone("1212121212");
		if(patients!=null)
		for(Object o : patients){
			LOGGER.info(o.toString());
		}
		
		LOGGER.info("Test passed");
	}
	
	/**
	 * Checks for patient's acceptance
	 */
	@Test
	@Ignore
	public void verifyPatientInfoTest(){
		int size=userService.verifyPatientInfo(1);
		LOGGER.info("Is accepted: "+size);
	}
	
	
	@Test
	@Ignore
	public void getUsersByOrgIdTest(){
		List<User> users = userService.getUsersByOrgId(285);
		LOGGER.info("team size" +users.size());
		
	}
	
	@Test
	public void getPatientPathwayBlockById(){
		JsonMessage message = userService.getPatientpathwayblockById(479);
		
		LOGGER.info(message.getMessage()+"  "+message.getData().toString()+"  "+message.getStatuscode());
	}
	
}
