package com.engage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 
 * @author mindtech-labs
 *
 */
@Component
@PropertySource("classpath:host.properties")
@Order(value = 5)
public class HostFilter extends GenericFilterBean {
	/**
	 * Logger implemetation
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HostFilter.class);

	/*
	 * @Autowired private Environment environment;
	 */

	/**
	 * Spring container wires valid.hosts from host.properties file
	 */
	@Value(value = "${valid.hosts}")
	private String[] allowedHosts;

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		final String host = req.getHeader("Host");
		// List of allowed hosts
		// allowedHosts =environment.getProperty("valid.hosts", String[].class);
		final List<String> listReferer = Arrays.asList(allowedHosts);

		if (host == null || listReferer.stream().noneMatch(s -> s.contains(host))) {
			// write logout logic here
			//ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		//	response = responseEntity;
			//((HttpServletResponse)response).sendRedirect("https://engage.quantifiedcare.com/users");
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOGGER.error("Invalid host: " + host);
		} else {
			LOGGER.info("Host " + host + " is valid");
			chain.doFilter(request, response);
		}

	}

}
