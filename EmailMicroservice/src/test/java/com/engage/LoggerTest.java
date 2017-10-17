package com.engage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringRunner.class)
public class LoggerTest {
	
	private static Logger logger = LoggerFactory.getLogger(LoggerTest.class);
	
	@Test
	public void emailTest() throws JsonProcessingException, Exception{
		

		logger.info("Error Report Test info");
		logger.warn("Error Report Test warn");
		
		logger.error("Error Report Test error");

		
	}

}
