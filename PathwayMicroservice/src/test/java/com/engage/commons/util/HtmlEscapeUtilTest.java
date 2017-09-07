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
	private static String escapedString ="";
	@Test
	public void checkProperties() throws JsonParseException, JsonMappingException, IOException{
	
		logger.info(HtmlEscapeUtil.escapeHtml("&<>\'\")(*!/"));
	}

	/**
	 * For checking  unescapeHtml method 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Before
	public void init() throws JsonParseException, JsonMappingException, IOException{
		escapedString = HtmlEscapeUtil.escapeHtml(unescapedString);
	}
	
	@Test
	public void verifyUnescapeHtml() throws JsonParseException, JsonMappingException, IOException{
	
		logger.info("Html Escape String: "+HtmlEscapeUtil.unescapeHtml(escapedString));
		String resultString = HtmlEscapeUtil.unescapeHtml(escapedString);
		logger.info("Html string: "+resultString);
		org.junit.Assert.assertTrue(resultString.equals(unescapedString));
	}
	
	
}
