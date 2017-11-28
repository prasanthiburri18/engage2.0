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

import com.engage.commons.dto.PathwayListDto;
import com.engage.commons.exception.PathwayNotFoundException;
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class PathwayServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientServiceTest.class);
	@Autowired
	private PathwayService pathwayService;
	@Test
	public void getPathwayListDtoTest() throws PathwayNotFoundException{
		Long orgId = new Long(69);
	List<PathwayListDto> pathwayListDtos = pathwayService.getPathwayList(orgId);
	LOGGER.info("Size of pathway list: "+pathwayListDtos.size());
	}
}
