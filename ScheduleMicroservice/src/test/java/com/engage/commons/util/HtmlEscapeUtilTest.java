package com.engage.commons.util;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringRunner.class)

public class HtmlEscapeUtilTest {
	/**
	 * Logger used here
	 */
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(HtmlEscapeUtilTest.class);

	private static String unescapedString = "&<>\'\")(*!/";
	private static String escapedString = "";

	@Test
	public void checkProperties() throws JsonParseException, JsonMappingException, IOException {

		logger.info(HtmlEscapeUtil.escapeHtml("&<>\'\")(*!/"));
	}

	/**
	 * For checking unescapeHtml method
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Before
	public void init() throws JsonParseException, JsonMappingException, IOException {
		escapedString = HtmlEscapeUtil.escapeHtml(unescapedString);
	}

	/**
	 * Convert back and check for correct conversion
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyUnescapeHtml() throws JsonParseException, JsonMappingException, IOException {

		logger.info("Html Escape String: " + HtmlEscapeUtil.unescapeHtml(escapedString));
		String resultString = HtmlEscapeUtil.unescapeHtml(escapedString);
		logger.info("Html string: " + resultString);
		org.junit.Assert.assertTrue(resultString.equals(unescapedString));
	}

	/**
	 * Pass null to eescape logic
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyEscapeHtmlWithNullString() throws JsonParseException, JsonMappingException, IOException {

		String resultString = HtmlEscapeUtil.escapeHtml(null);
		logger.info("Html string: " + resultString);
		org.junit.Assert.assertTrue(resultString == null);
	}

	/**
	 * Pass null to unescape logic
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyUnescapeHtmlWithNullString() throws JsonParseException, JsonMappingException, IOException {

		String resultString = HtmlEscapeUtil.unescapeHtml(null);
		logger.info("Html string: " + resultString);
		org.junit.Assert.assertTrue(resultString == null);
	}

	/**
	 * Script and delete removal test
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyBlacklistRemoval() throws JsonParseException, JsonMappingException, IOException {
		String testString = "<script> delete* abcd";
		String resultString = HtmlEscapeUtil.removeBlacklistedWords(testString);
		logger.info("Result string: " + resultString);
		org.junit.Assert.assertTrue(resultString.equals("<> * abcd"));
	}

	/**
	 * Ignore case test in script and delete
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyBlacklistRemoval2() throws JsonParseException, JsonMappingException, IOException {
		String testString = "Abcd<SCriPt> deLetE* abcd";
		String resultString = HtmlEscapeUtil.removeBlacklistedWords(testString);
		logger.info("Result string: " + resultString);
		org.junit.Assert.assertTrue(resultString.equals("Abcd<> * abcd"));
	}

	/**
	 * Checking for \s escape character
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyBlacklistRemoval3() throws JsonParseException, JsonMappingException, IOException {
		String testString = "Abcd<SCriPt> deLetE* abcd <\\ScriPT> <\\scriPT>";
		String resultString = HtmlEscapeUtil.removeBlacklistedWords(testString);
		logger.info("Result string: " + resultString);
		org.junit.Assert.assertTrue(resultString.equals("Abcd<> * abcd <\\> <\\>"));
	}

	/**
	 * Checking passing null string
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyBlacklistRemoval4() throws JsonParseException, JsonMappingException, IOException {
		String testString = null;
		String resultString = HtmlEscapeUtil.removeBlacklistedWords(testString);
		logger.info("Result string: " + resultString);
		org.junit.Assert.assertTrue(resultString == null);
	}

	/**
	 * Verifying with smaller string
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyBlacklistRemoval5() throws JsonParseException, JsonMappingException, IOException {
		String testString = "scri";
		String resultString = HtmlEscapeUtil.removeBlacklistedWords(testString);
		logger.info("Result string: " + resultString);
		org.junit.Assert.assertTrue(resultString.equals(testString));
	}
	
	
	/**
	 * backtoback
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void verifyBlacklistRemoval6() throws JsonParseException, JsonMappingException, IOException {
		String testString = "deletescript";
		String resultString = HtmlEscapeUtil.removeBlacklistedWords(testString);
		logger.info("Result string: " + resultString);
		org.junit.Assert.assertTrue(resultString.equals(""));
	}
}
