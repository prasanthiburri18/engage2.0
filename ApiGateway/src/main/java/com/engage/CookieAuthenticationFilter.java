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

@Order(value = 2)
public class CookieAuthenticationFilter extends GenericFilterBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CookieAuthenticationFilter.class);

	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";

	/**
	 * 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		LOGGER.info("Http Version " + request.getProtocol());

		String requestUrl = req.getRequestURI();
		String basicAuthHeader = req.getHeader("Authorization");

		Cookie[] requestCookies = req.getCookies();
		Cookie authCookie = null;
		Cookie refreshCookie = null;
		
		if (requestCookies != null ) {
			for (Cookie cookie : requestCookies) {

				if (cookie.getName().equals("Authorization")) {
					authCookie = cookie;
				} else if (cookie.getName().equals("refresh_token")) {
					refreshCookie = cookie;
				}
			}
			if(authCookie==null||refreshCookie==null){
				LOGGER.info("Auth token / refresh cookie is not passed");
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);	
			}
			else if (authCookie.getValue() == null || refreshCookie.getValue() == null || authCookie.getValue() == ""
					|| refreshCookie.getValue() == "") {
				LOGGER.info("Auth token / refresh token is null");
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);

			}
			else{
				chain.doFilter(request, response);
			}
		}
		else{
			LOGGER.info("Protected Url access without credentials: "+req.getRequestURI()+" "+req.getUserPrincipal());
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
		}
	
	}
}
