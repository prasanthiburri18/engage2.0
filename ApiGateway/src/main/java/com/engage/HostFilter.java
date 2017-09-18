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
@PropertySource("classpath:host.properties")
public class HostFilter extends GenericFilterBean {
	/**
	 * Logger implemetation
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HostFilter.class);
	
	/*@Autowired
	private Environment environment;
	*/
	
	/**
	 * Spring container wires valid.hosts from host.properties file
	 */
	@Value(value="${valid.hosts}")
	private String[] allowedHosts;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		final String host = req.getHeader("Host");
		//allowedHosts =environment.getProperty("valid.hosts", String[].class);
		List<String> listReferer = Arrays.asList(allowedHosts);
		
		if (host==null|| listReferer.stream().noneMatch(s->s.contains(host))){
			// write logout logic here
		
			LOGGER.error("Invalid host: "+host);
		}
		else{
			LOGGER.info("Host "+host+" is valid");
			chain.doFilter(request, response);
		}

	}

}
