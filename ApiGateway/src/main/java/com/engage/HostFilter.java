package com.engage;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.engage.dao.IHostDao;
import com.engage.model.Host;

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
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HostFilter.class);

	@Autowired
	private IHostDao hostDao;

	/**
	 * Spring container wires valid.hosts from host.properties file
	 */

	private String[] allowedHosts;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		final String host = req.getHeader("Host");
		final String customHeader = req.getHeader("X-Requested-With");
		LOGGER.info("custom header " + customHeader);
		if (customHeader == null
				|| !customHeader.equalsIgnoreCase("XMLHttpRequest")) {
			LOGGER.error("Not an ajax call");
			((HttpServletResponse) response)
					.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);

		}

		
		List<Host> hosts = hostDao.findAll();
		
		if(host==null||hosts.stream().noneMatch(h->h.getHost().contains(host))){
			((HttpServletResponse) response)
			.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
	LOGGER.info("Invalid host header passed to access "
			+ req.getRequestURI());
	LOGGER.error("Invalid host: " + host);
		} else {
			LOGGER.info("Host " + host + " is valid");
			chain.doFilter(request, response);
		}
		
	}

}
