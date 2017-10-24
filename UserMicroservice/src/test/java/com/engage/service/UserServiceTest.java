package com.engage.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
	@Autowired
	private UserService userService;

	@Test
	public void getPatientByDobTest(){
		userService.verifyPatientInfobydob("1993-02-13");
		LOGGER.info("Test passed");
	}
}
