package com.engage.util;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.dao.EncryptKeyDao;

/**
 * Test is written to check whether {@link EncryptKeyDao} is autowired to
 * {@link AdvancedEncryptionStandard} class
 * And whether encryption and decryption is working
 * @author mindtech-labs
 *
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class EncryptionUtilTest {
	private String testString;
	private Logger logger = LoggerFactory.getLogger(EncryptionUtilTest.class);

	@Autowired
	private AdvancedEncryptionStandard aes;

	/**
	 * Test for encryption and autowiring status of {@link EncryptKeyDao}
	 */
	@Test
	public void testEncryptKey() {
		logger.info("Autowiring Status ");
		logger.info(AdvancedEncryptionStandard.getEncryptionKey());
		Assert.assertTrue(aes != null);

	}

	/**
	 * Initializing the test string
	 * 
	 * @throws Exception
	 */
	@Before
	public void init() {
		testString = "abcdee 133l*";

		logger.info("Before block encrypted string " + testString);
	}

	@Test
	public void testEncryption() throws Exception {
		String encrypted = AdvancedEncryptionStandard.encrypt(testString);
		logger.info("Encrypted String " + encrypted);
		String decrypted = AdvancedEncryptionStandard.decrypt(encrypted);
		Assert.assertTrue(decrypted.equals(testString));

	}
	
}
