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

import com.engage.dto.PathwayAndEventNames;

/**
 * @author mindtech-labs
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class PathwayAndEventNamesTest {
	private static final Logger logger = LoggerFactory.getLogger(PathwayAndEventNamesTest.class);
	@Autowired
	private PathwayService pathwayService;
	
	@Test
	public void getPathwayAndEventNamesListTest(){
		Long orgId = new Long(285);
		logger.info("Org Id passed "+orgId);
		List<PathwayAndEventNames> list = pathwayService.getPathwayEventNamesAndAcceptedStatus(orgId);
		logger.info("Size of list: "+list.size());
		
	}
}
