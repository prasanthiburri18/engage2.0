package com.engage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationConfigTest {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigTest.class);
	@Test
	public void applicationConfigTest(){
		logger.info("Application started");
		
	}
}
