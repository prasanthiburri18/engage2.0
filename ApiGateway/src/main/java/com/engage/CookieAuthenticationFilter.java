/**
 * 
 */
package com.engage;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
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
@Order(value = 2)
public class CookieAuthenticationFilter extends GenericFilterBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CookieAuthenticationFilter.class);
	
	private static final String OAUTH2_TOKEN_URL="/ApiGateway/users/oauth/token";
	/**
	 * 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		chain.doFilter(request, response);
		
	}

}
