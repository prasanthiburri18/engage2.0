/**
 * 
 */
package com.engage.controller;

import javax.transaction.Transactional;

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

import com.engage.dao.BlocksDao;
import com.engage.model.Blocks;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@Transactional
public class BlocksControllerTest {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(BlocksControllerTest.class);
	
	@Autowired
	private MockMvc mockmvc;

	@Autowired
	private  BlocksDao blockDao;
	
	
	private ObjectMapper objectMapper = new ObjectMapper();
	

	/**
	 * To check functionality from Engage1.0
	 * @throws Exception 
	 * @throws JsonProcessingException 
	 */
	@Test
	public void userAlreadyExistsTestWithValidUser() throws JsonProcessingException, Exception{
		Blocks blocks = new Blocks();
		blocks.setBodyOfMessage("EDD<crript> script");
		
		MvcResult result =mockmvc.perform(
				MockMvcRequestBuilders.post("/api/v1/addBlock")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(blocks)))
				.andReturn();
				//.andExpect(MockMvcResultMatchers.status().is(203));
		logger.info(result.getResponse().getContentAsString());
		org.junit.Assert.assertTrue(result.getResponse().getContentAsString().indexOf("204")>0);
	
	}
	

}
