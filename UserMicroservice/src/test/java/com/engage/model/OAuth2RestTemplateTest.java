package com.engage.model;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OAuth2RestTemplateTest {
	
	private static final Logger logger = LoggerFactory.getLogger(OAuth2RestTemplateTest.class);
	
	@Autowired
	private OAuth2RestTemplate restTemplate;
	
	@Value("${emailMicroService.URL}")
	private String emailMicroserviceURL;
	@Test
	public void testRestTemplate(){

		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("from", "s.krishna@mindtechlabs.com");
		data1.put("to", "s.krishna@mindtechlabs.com");
		data1.put("subject", "Rest Template Test Email");
		data1.put("text",
				"Hi, Rest Template Test");
		data1.put("status", true);
		OAuth2AccessToken accessToken = restTemplate.getAccessToken();
		logger.info("Access Token" +accessToken);
		restTemplate.postForObject(emailMicroserviceURL + "/email/send", data1, String.class);
	logger.info("Mail sent");
	}
}
