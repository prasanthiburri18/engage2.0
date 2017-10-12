/**
 * 
 */
package com.engage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author mindtech-labs
 *
 */
@Component
@Order(value=2)
public class OriginFilter extends GenericFilterBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OriginFilter.class);
	private static String[] validOrigins;
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	HttpServletRequest req = (HttpServletRequest) request;
	

	String origin = req.getHeader("origin");
	
	final FileInputStream blackListFile = new FileInputStream(
			OriginFilter.class.getClassLoader().getResource("origin.properties").getFile());
	final Properties props = new Properties();
	props.load(blackListFile);
	validOrigins = props.getProperty("valid.origins").split(",");
	boolean isValidOrigin=false;
	if(origin!=null&&!origin.equals(""))
	for (String o: validOrigins){
		if(origin.contains(o)){
		isValidOrigin=true;
		}
	}
	
	if(isValidOrigin){
		LOGGER.info(origin+ " is a valid origin");
		chain.doFilter(request, response);
	}
	else{
		((HttpServletResponse)response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
		LOGGER.error("Invalid origin: " + origin);
	}
	
	}

}
