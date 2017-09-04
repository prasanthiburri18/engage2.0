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
public class ApplicationConfigurationTest {
	//Additional configuration to wire ServerProp as ZuulConfiguration is conditional on web app
	
	/*@EnableZuulServer
	static class TestConfiguration{
		@Bean
		@ConfigurationProperties
		public ServerProperties getServerProperties(){
			return new ServerProperties();
		}
	}*/
	private static final  Logger logger = LoggerFactory.getLogger(ApplicationConfigurationTest.class);
	
	@Test@Ignore
	public void testConfigTest(){
		logger.info(" Application started");
	}
}
