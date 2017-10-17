package com.engage;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.model.ValidateOrganizationEntityTest;
import com.engage.model.ValidateUserTest;

@RunWith(SpringRunner.class)
@SpringBootTest
@SuiteClasses(value={ValidateUserTest.class, ValidateOrganizationEntityTest.class})
@Ignore
public class ApplicationConfigurationTest {

	@Test
	public void appConfigurationTest(){
		
	}
	
}
