/**
 * 
 */
package com.engage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author mindtech-labs
 *
 */
@Component
public class ResponseInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseInterceptor.class);
	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpServletResponse httpResponse = response;
		String requestUrl = request.getRequestURI();
		if (requestUrl.contains(OAUTH2_TOKEN_URL)&&httpResponse.getStatus()==200) {

			String value = "oauth2 cookie";
			Cookie cookie = new Cookie("AuthorizationToken", value);
			
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			httpResponse.addCookie(cookie);
	LOGGER.info(httpResponse.getContentType().toString());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HttpServletResponse httpResponse = response;
		String requestUrl = request.getRequestURI();
		if (requestUrl.contains(OAUTH2_TOKEN_URL)&&httpResponse.getStatus()==200) {

			String value = "oauth2 cookie";
			Cookie cookie = new Cookie("AuthorizationToken", value);
			
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			httpResponse.addCookie(cookie);
	LOGGER.info(httpResponse.getContentType().toString());
		}

		String value = "Some cookie";
		Cookie cookie = new Cookie("Test ", value);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		cookie.setMaxAge(1000000);
		httpResponse.addCookie(cookie);

	}
	
	
	

}
