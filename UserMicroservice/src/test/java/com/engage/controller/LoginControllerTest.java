package com.engage.controller;

import javax.transaction.Transactional;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.dao.UserDao;
import com.engage.dao.UserRolesDao;

/**
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers={LoginController.class, UserDao.class, UserRolesDao.class }, excludeAutoConfiguration={SecurityAutoConfiguration.class})
@Transactional

public class LoginControllerTest {

}
