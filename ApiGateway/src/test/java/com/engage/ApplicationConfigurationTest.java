package com.engage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.cloud.netflix.zuul.ZuulConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
@ActiveProfiles(profiles={"local"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ ZuulConfiguration.class, Application.class})
public class ApplicationConfigurationTest {
	//Additional configuration to wire ServerProp as ZuulConfiguration is conditional on web app
	@WebAppConfiguration
	@EnableZuulServer
	static class TestConfiguration{
		@Bean
		@ConfigurationProperties
		public ServerProperties getServerProperties(){
			return new ServerProperties();
		}
	}
	private static final  Logger logger = LoggerFactory.getLogger(ApplicationConfigurationTest.class);
	@Test
	public void testConfigTest(){
		logger.info(" Application started");
	}
}
