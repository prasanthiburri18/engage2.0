package com.engage.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>This is used for removing predefined blacklisted words and also for
 * converting htmlChars to htmlCode ( both back and forth). </p>
 * <i>Note: Project where
 * this class is used, must contain a sanitation.json and blacklist.properties
 * files in resources folder.
 * </i>
 * @author mindtechlabs
 *
 */
public class HtmlEscapeUtil {

	private static Map<String, String> whitelist;

	private static String[] blacklist;

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(HtmlEscapeUtil.class);

	/**
	 * Whitelist consists of symbols and their html codes
	 * 
	 * On escapHtml, all the symbols are replaced by html codes
	 * 
	 * Included removeBlacklistedWords() function logic as well
	 * 
	 * @param htmlString
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	public static synchronized String escapeHtml(String htmlString)
			throws JsonParseException, JsonMappingException, IOException {
		if (!(htmlString == null || htmlString.equals(""))) {
			Set<String> htmlChars = getWhitelist().keySet();
			logger.debug("Is set size empty" + htmlChars.isEmpty());

			// Remove blacklisted words
			htmlString = removeBlacklistedWords(htmlString);
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
		// values of whitelist
		if (!(htmlString == null || htmlString.equals(""))) {
			ArrayList<String> htmlChars = (ArrayList<String>) getWhitelist().values().stream()
					.collect(Collectors.toList());

			// Reverse order of values in whitelist
			Collections.reverse(htmlChars);

			LinkedHashMap<String, String> reverseWhiteList = new LinkedHashMap<>();
			// Reverse key-value of whitelist to value-key and add to other map
			whitelist.forEach((key, value) -> {
				reverseWhiteList.put(value, key);
			});

			logger.debug("Is set size empty" + htmlChars.isEmpty());
			// String escapedString = null;;

			for (String str : htmlChars) {
				htmlString = htmlString.replaceAll(str, reverseWhiteList.get(str));
			}

		}
		return htmlString;
	}
/**
 * <p>Gets whitelist from json file to whitelist map object</p>
 * @return
 * @throws JsonParseException
 * @throws JsonMappingException
 * @throws IOException
 */
	private static synchronized Map<String, String> getWhitelist()
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
	/**
	 * <p>Loads blacklisted words and removes those words from a test string passed to this function</p>
	 * @param evilString
	 * @return
	 * @throws IOException
	 */
	public static synchronized String removeBlacklistedWords(String evilString) throws IOException {
		if (!(evilString == null || evilString.equals(""))) {
			if (blacklist == null) {

				final FileInputStream blackListFile = new FileInputStream(
						HtmlEscapeUtil.class.getClassLoader().getResource("blacklist.properties").getFile());
				final Properties props = new Properties();
				props.load(blackListFile);
				blacklist = props.getProperty("blacklist.words").split(",");
			}
			for (String str : blacklist) {
				evilString = evilString.replaceAll("(?i)" + str.trim(), "");
			}
		}
		return evilString;
	}
}
