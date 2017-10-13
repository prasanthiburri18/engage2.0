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
		try{
			final FileInputStream blackListFile = new FileInputStream(
					RefererFilter.class.getClassLoader().getResource("host.properties").getFile());
			final Properties props = new Properties();
			props.load(blackListFile);
			allowedHosts = props.getProperty("valid.hosts").split(",");
			if(allowedHosts==null||allowedHosts.length<1){
				throw new PropertyLoadingException("Host properties not configured properly");
			}
			}catch(Exception ex){
				
				LOGGER.error("Please configure host properties");
				((HttpServletResponse)response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
				
			}
		
		
		final List<String> listReferer = Arrays.asList(allowedHosts);

		if (host == null || listReferer.stream().noneMatch(s -> s.contains(host))) {
			
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOGGER.info("Invalid host header passed to access "+req.getRequestURI());
			LOGGER.error("Invalid host: " + host);
		} else {
			LOGGER.info("Host " + host + " is valid");
			chain.doFilter(request, response);
		}

	}

}
