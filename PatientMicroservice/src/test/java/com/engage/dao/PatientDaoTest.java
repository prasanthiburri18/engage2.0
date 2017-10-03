package com.engage.dao;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.model.Patient;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class PatientDaoTest {
	@Autowired
	private PatientDao dao;

	@Test
	@Transactional
	public void savePatientTest() {
		Patient p = new Patient("adf@kd", "adbfl", "adfdf", "9663908949", "abcd", "Y", null, null, null, 1, 1);

		long id = dao.save(p);
	Assert.assertTrue(id>0);
	}

}
