package com.engage.commons.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HtmlEscapeUtil {

	private static Map<String, String> whitelist;

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(HtmlEscapeUtil.class);

	/**
	 * Whitelist consists of symbols and their html codes
	 * 
	 * On escapHtml, all the symbols are replaced by html codes
	 * 
	 * @param htmlString
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	public static synchronized String escapeHtml(String htmlString)
			throws JsonParseException, JsonMappingException, IOException {

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

	/**
	 * Use this method for converting html codes to html character
	 * 
	 * @param htmlString
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static synchronized String unescapeHtml(String htmlString)
			throws JsonParseException, JsonMappingException, IOException {
		//values of whitelist
		ArrayList<String> htmlChars = (ArrayList<String>) getWhitelist().values().stream().collect(Collectors.toList());

		htmlChars.forEach(h -> logger.info(h));
		// Reverse order of values in whitelist
		Collections.reverse(htmlChars);

		LinkedHashMap<String, String> reverseWhiteList = new LinkedHashMap<>();
		//Reverse  key-value of whitelist to value-key and add to other map
		whitelist.forEach((key, value) -> {
			reverseWhiteList.put(value, key);
		});

		logger.debug("Is set size empty" + htmlChars.isEmpty());
		// String escapedString = null;;
		if (!(htmlString == null || htmlString.equals(""))) {
			for (String str : htmlChars) {
				htmlString = htmlString.replaceAll(str, reverseWhiteList.get(str));
			}

		}
		return htmlString;
	}

	public static synchronized Map<String, String> getWhitelist()
			throws JsonParseException, JsonMappingException, IOException {
		if (whitelist == null) {
			ObjectMapper mapper = new ObjectMapper();
			ClassLoader classLoader = HtmlEscapeUtil.class.getClassLoader();
			File file = new File(classLoader.getResource("sanitation.json").getFile());
			// File file = ResourceUtils.getFile("classpath:/sanitation.json");
			whitelist = mapper.readValue(file, new TypeReference<LinkedHashMap<String, String>>() {
			});
		}
		return whitelist;
	}

	public static synchronized String removeBlacklistedString(String evilString) {

		String goodString = "";

		return goodString;
	}
}
