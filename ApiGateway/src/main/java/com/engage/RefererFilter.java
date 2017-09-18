package com.engage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
@Component
@PropertySource("classpath:referer.properties")
public class RefererFilter extends GenericFilterBean {
	/**
	 * Logger implemetation
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RefererFilter.class);
	
	/*@Autowired
	private Environment environment;
	
	*/
	/**
	 * Spring container wires valid.referers from referer.properties file
	 */
	@Value(value="${valid.referers}")
	private String[] allowedReferers;
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		final String referer = req.getHeader("Referer");
		LOGGER.error("Invalid referer: "+referer);
		//allowedReferers =environment.getProperty("valid.referers", String[].class);
		List<String> listReferer = Arrays.asList(allowedReferers);
		
		if (referer==null|| listReferer.stream().noneMatch(s->referer.contains(s))){
			//logout logic
		
			LOGGER.error("Invalid referer: "+referer);
		}
		else{
			chain.doFilter(request, response);
		}

	}

}
