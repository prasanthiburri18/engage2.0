package com.engage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={Application.class})
//@WebIntegrationTest
@WebAppConfiguration
@Ignore
public class HostFilterTest {
	
	 @Autowired
	    private WebApplicationContext wac;
	
	private MockMvc mockmvc;
	@Before
	public void init(){
		mockmvc =MockMvcBuilders.webAppContextSetup(wac).build();
	}
	@Test
	public void testHostFilter() throws Exception {
	MvcResult result = mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/world")).andReturn();
	result.getResponse().getContentAsString().contains("world");
		
	}

}
