package com.engage;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={Application.class})
@Ignore
public class LoggerTest {
	//Local class level logger
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTest.class);
			
	@Test
	public void errorLogTest(){
		LOGGER.error("Error Report Test");
	}
	
}
