package com.engage.commons.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class HtmlEscapeUtil {

	private  static Map<String, String> whitelist;
	

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(HtmlEscapeUtil.class);
	
	

	public static synchronized String escapeHtml(String htmlString) throws JsonParseException, JsonMappingException, IOException {
		

		Set<String> htmlChars = getWhitelist().keySet();
		logger.debug("Is set size empty" + htmlChars.isEmpty());
		// String escapedString = null;;
		if (!(htmlString == null || htmlString.equals(""))) {
			for (String str : htmlChars) {
				htmlString = htmlString.replaceAll(str, whitelist.get(str));
			}

		}
		return htmlString;
	}
	
	
	public static synchronized Map<String, String> getWhitelist() throws JsonParseException, JsonMappingException, IOException {
		if(whitelist==null){
			ObjectMapper mapper = new ObjectMapper();
			ClassLoader classLoader = HtmlEscapeUtil.class.getClassLoader();
			File file = new File(classLoader.getResource("sanitation.json").getFile());
			//File file = ResourceUtils.getFile("classpath:/sanitation.json");
			whitelist = mapper.readValue(file, new TypeReference<LinkedHashMap<String, String>>() {
			});
		}
		return whitelist;
	}

}
