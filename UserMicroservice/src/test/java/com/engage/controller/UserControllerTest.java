/**
 * 
 */
package com.engage.controller;

import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.engage.dao.UserDao;
import com.engage.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
//@WebMvcTest(controllers={LoginController.class, UserController.class, UserService.class, UserDao.class, IUserDao.class, UserRolesDao.class }, excludeAutoConfiguration={SecurityAutoConfiguration.class})
@SpringBootTest
@Transactional
@Ignore
public class UserControllerTest {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserControllerTest.class);
	
	@Autowired 
	private LoginController loginController;
	
	@Autowired
	private MockMvc mockmvc;

	@Autowired
	private UserDao userDao;
	
	
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Before
	public void init(){
		
		final User user = new User();
		user.setEmail("s.krishna1@mindtechlabs.com");
		user.setFullName("Somisetti Krishna");
		user.setOrgid(1);
		user.setPassword("password");
		user.setPhone("1234567890");
		user.setPracticeName("Medicine");
		user.setUserType("U");
		userDao.save(user);
	}
	
	

	@Test
	public void passwordChangeWithoutPhone(){
		
		 User user = userDao.getById("s.krishna1@mindtechlabs.com");
		
	
		user.setFullName("Somisetti Krishna");
		user.setPassword("password");
		user.setPhone("");
		user.setPracticeName("Medicine");
		user.setUserType("U");
		userDao.update(user);
	}
	
	/**
	 * To check functionality from Engage1.0
	 * @throws Exception 
	 * @throws JsonProcessingException 
	 */
	@Test
	public void userAlreadyExistsTestWithValidUser() throws JsonProcessingException, Exception{
		final User user = new User();
		user.setEmail("s.krishna@mindtechlabs.com");
		user.setFullName("Somisetti Krishna");
		user.setOrgid(1);
		user.setPassword("password");
		user.setPhone("1234567890");
		user.setPracticeName("Medicine");
		user.setUserType("U");
		MvcResult result =mockmvc.perform(
				MockMvcRequestBuilders.post("/api/v1/addteammember")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user)))
				.andReturn();
				//.andExpect(MockMvcResultMatchers.status().is(203));
		logger.info(result.getResponse().getContentAsString());
		org.junit.Assert.assertTrue(result.getResponse().getContentAsString().indexOf("208")>0);
	
	}
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
		//Due to design flaw in Engage1.0. Status of response is 200.
		org.junit.Assert.assertTrue(result.getResponse().getContentAsString().indexOf("400")>0);
		
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
		user.setPassword("");
		user.setPhone("1234567890");
		user.setPracticeName("Medicine");
		user.setUserType("U");
		
		MvcResult mvcResult =mockmvc.perform(
				MockMvcRequestBuilders.post("/api/v1/addteammember")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user)))
				.andReturn();
		logger.info("Test info "+mvcResult.getResponse().getContentAsString());
		
		org.junit.Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
		org.junit.Assert.assertTrue(mvcResult.getResponse().getContentAsString().indexOf("Team Member added successfully")>0);
	}
	/**
	 * Invalid Phone format
	 * @throws Exception
	 */
	@Test
	public void userAdditionInvalidPhoneFormatTest() throws Exception {
		final User user = new User();
		user.setEmail("afdk@kdf.com");
		user.setFullName("Somisetti Krishna");
		user.setOrgid(1);
		user.setPassword("password");
		//9 digits
		user.setPhone("123456780");
		user.setPracticeName("Medicine");
		user.setUserType("U");
		
		MvcResult mvcResult =mockmvc.perform(
				MockMvcRequestBuilders.post("/api/v1/addteammember")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user)))
				.andReturn();
		logger.info("Test info "+mvcResult.getResponse().getContentAsString());
		
		org.junit.Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
		org.junit.Assert.assertTrue(mvcResult.getResponse().getContentAsString().indexOf("Invalid phone number format")>0);
	}
	
	@Test
	public void userAdditionInvalidFullNameFormatTest() throws Exception {
		final User user = new User();
		user.setEmail("afdk@kdf.com");
		user.setFullName("6Somisetti Krishna");
		user.setOrgid(1);
		user.setPassword("password");
		//9 digits
		user.setPhone("123456780");
		user.setPracticeName("Medicine");
		user.setUserType("U");
		
		MvcResult mvcResult =mockmvc.perform(
				MockMvcRequestBuilders.post("/api/v1/addteammember")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user)))
				.andReturn();
		logger.info("Test info "+mvcResult.getResponse().getContentAsString());
		
		Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
		Assert.assertTrue(mvcResult.getResponse().getContentAsString().indexOf("Invalid phone number format")>0);
		Assert.assertTrue(mvcResult.getResponse().getContentAsString().indexOf("Only alphabet characters are allowed.")>0);
	}
	
	
	@Test
	public void userRegisterTest() throws Exception {
		final User user = new User();
		user.setEmail("afdk@kdf.com");
		user.setFullName("Somisetti Krishna");
		//user.setOrgid(1);
		user.setPassword("password");
		//9 digits
		user.setPhone("1234567890");
		user.setPracticeName("Medicine");
		user.setUserType("A");
		
	/*	MvcResult mvcResult =mockmvc.perform(
				MockMvcRequestBuilders.post("/registration")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(user)))
				.andReturn();
		logger.info("Test info "+mvcResult.getResponse().getContentAsString());
		
		Assert.assertEquals(mvcResult.getResponse().getStatus(), 200);
		Assert.assertTrue(mvcResult.getResponse().getContentAsString().indexOf("Invalid phone number format")>0);
		Assert.assertTrue(mvcResult.getResponse().getContentAsString().indexOf("Only alphabet characters are allowed.")>0);
	*/
	
	
		loginController.create(user);
		
		
	}
	
	
	}
