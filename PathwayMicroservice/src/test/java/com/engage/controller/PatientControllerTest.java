package com.engage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest
@Transactional

public class PatientControllerTest {
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PatientControllerTest.class);
	
	@Autowired
	private MockMvc mockmvc;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void updatePatientPathwayBlockTest() throws JsonProcessingException, Exception{
	
	/*blockid
	:
	1024
	messagetxt
	:
	"Hi #FN  you have been chosen to receive messages from the engage application.  To accept reply with Y, to decline reply with N"
	mtype
	:
	"M"
	patientid
	:
	"275"
	*/
	Map<String, String> testMap= new HashMap<>();
	testMap.put("blockid","1024");
	testMap.put("messagetxt","testString");
	testMap.put("patientid","275");
	testMap.put("mtype","M");

MvcResult result =mockmvc.perform(
		MockMvcRequestBuilders.put("/api/v1/updatePatientPathwayblock")
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsBytes(testMap)))
		.andReturn();
		//.andExpect(MockMvcResultMatchers.status().is(203));
logger.info(result.getResponse().getContentAsString());
org.junit.Assert.assertTrue(result.getResponse().getContentAsString().indexOf("204")>0);
	}
}
