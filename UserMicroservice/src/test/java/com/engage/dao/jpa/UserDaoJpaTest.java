package com.engage.dao.jpa;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.model.User;

import io.jsonwebtoken.lang.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoJpaTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoJpaTest.class);
	@Autowired
	private IUserDao userDaoJpa;
	
	@Test
	public void getUsersByOrgIdTest(){
		
		
		List<User> users = userDaoJpa.getUserByOrgid(322);
		
		Assert.notEmpty(users);
	}
	
	@Test
	public void getAllPracticeNameTest(){
		List<String> practiceNames = userDaoJpa.getAllPracticeNames();
		LOGGER.info(practiceNames.toString());
		
		Assert.notEmpty(practiceNames);
		
	}
	
	@Test
	public void findByEmailIgnoreCaseTest(){
		
		LOGGER.info("Getting users by email");
		User user = userDaoJpa.findUserByEmailIgnoreCase("quantifiedcar111@gmail.com");
		LOGGER.info(user.toString());
		Assert.notNull(user);
	}
	
}
