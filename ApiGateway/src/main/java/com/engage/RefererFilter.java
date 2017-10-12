package com.engage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.engage.exception.PropertyLoadingException;

/**
 * 
 * @author mindtech-labs
 *
 */
@Component
@PropertySource("classpath:referer.properties")
//@ConfigurationProperties(prefix="valid")
@Order(value = 4)
public class RefererFilter extends GenericFilterBean {
	/**
	 * Logger implemetation
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RefererFilter.class);

	/*
	 * @Autowired private Environment environment;
	 * 
	 */
	/**
	 * Spring container wires valid.referers from referer.properties file
	 */

	private String[] allowedReferers;
	//@Value(value = "${valid.referers}")
	//private List<String> allowedReferers=new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		final String referer = req.getHeader("Referer");
		try{
		final FileInputStream blackListFile = new FileInputStream(
				OriginFilter.class.getClassLoader().getResource("referer.properties").getFile());
		final Properties props = new Properties();
		props.load(blackListFile);
		allowedReferers = props.getProperty("valid.referers").split(",");
		}catch(Exception ex){
			
			LOGGER.error("Please configure referer properties");
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			
		}
	
		final List<String> listReferer = Arrays.asList(allowedReferers);

		if (referer == null || listReferer.stream().noneMatch(s -> referer.contains(s))) {
			
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			
			LOGGER.error("Invalid referer: " + referer);
		} else {
			LOGGER.info("Referer "+referer+" is valid");
			chain.doFilter(request, response);
		}

	}

}
