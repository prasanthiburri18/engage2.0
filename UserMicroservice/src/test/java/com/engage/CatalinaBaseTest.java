package com.engage;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class CatalinaBaseTest {
	
	private Logger LOG = org.slf4j.LoggerFactory.getLogger(CatalinaBaseTest.class);
	@Value("${tomcat.home}")
	private String tomcatHome;
	
	@Test
	public void catalinaHomeTest(){
		LOG.info(tomcatHome+" tomcatHome");
		LOG.info(tomcatHome+" tomcatHome");
	}
	
}
