package com.engage.util;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringRunner;

import com.engage.commons.util.HtmlEscapeUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringRunner.class)

public class HtmlEscapeUtilTest {
	
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(HtmlEscapeUtilTest.class);


	@Test
	public void checkProperties() throws JsonParseException, JsonMappingException, IOException{
	
		logger.info(HtmlEscapeUtil.escapeHtml("&<>\'\")(*!/"));
	}
	
}
