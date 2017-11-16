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

import com.engage.dto.PathwayPatientBlockDto;
import com.engage.exception.PatientPathwayBlockNotFoundException;
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class PatientServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientServiceTest.class);
	@Autowired
	private PathwayService pathwayService;
	@Test
	public void getPatientPathwayBlockByIdTest() throws PatientPathwayBlockNotFoundException{
		List<PathwayPatientBlockDto> list = pathwayService.getPatientpathwayblockById(1);
		
		LOGGER.info("Is List empty? "+ (list==null?0:list.size()));
		
	}
}
