/**
 * 
 */
package com.engage.controller;

import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.engage.dao.UserDao;
import com.engage.dao.UserRolesDao;
import com.engage.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;

/**
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers={UserController.class, UserDao.class, UserRolesDao.class }, excludeAutoConfiguration={SecurityAutoConfiguration.class})
@Transactional
public class UserControllerTest {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserControllerTest.class);
	
	@Autowired
	MockMvc mockmvc;

	@Autowired
	UserDao userDao;
	
	ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * Checking whether mock beans are created
	 */
	@Test
	public void autoWiringTest() {
		assertNotNull(userDao);
	}

	@Test
	public void emptyUserAdditionTest() throws Exception {
		final User user = new User();
		MvcResult result =mockmvc.perform(
				MockMvcRequestBuilders.post("/api/v1/addteammember")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user)))
				.andReturn();
				//.andExpect(MockMvcResultMatchers.status().is(203));
		logger.info(result.getResponse().getContentAsString());
		org.junit.Assert.assertTrue(result.getResponse().getContentAsString().indexOf("203")>0);
		
	}
	/**
	 * Positive Test
	 * @throws Exception
	 */
	@Test
	public void userAdditionTest() throws Exception {
		final User user = new User();
		user.setEmail("afdk@kdf.com");
		user.setFullName("Somisetti Krishna");
		user.setOrgid(1);
		user.setPassword("password");
		user.setPhone("1234567890");
		user.setPracticeName("Medicine");
		user.setUserType("U");
		
		MvcResult mvcResult =mockmvc.perform(
				MockMvcRequestBuilders.post("/api/v1/addteammember")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user)))
				.andReturn();
		logger.info("Test info "+mvcResult.getResponse().getContentAsString());
		
	}

}
