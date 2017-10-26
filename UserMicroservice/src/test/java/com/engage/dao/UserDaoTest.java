package com.engage.dao;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class UserDaoTest {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);
	@Autowired 
	private UserDao userDao;
	
	@Test
	public void unwantedDaoFunctionsTest(){
		
	}
}
