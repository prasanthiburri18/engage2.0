package com.engage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={Application.class})

public class RefererFilterTest {
	
	@Autowired
	private MockMvc mockmvc;
	@Test
	public void testRefererFilter() throws Exception {
	MvcResult result = mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/world")).andReturn();
	result.getResponse().getContentAsString().contains("world");
		
	}

}
