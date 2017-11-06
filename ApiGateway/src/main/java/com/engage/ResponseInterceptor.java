/**
 * 
 */
package com.engage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
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
//	private static final String REFRESH_TOKEN_ENDPOINT = "http//:"
	@Autowired 
	private OAuth2RestTemplate restTemplate;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpServletResponse httpResponse = response;
		String requestUrl = request.getRequestURI();
		
		Cookie[] requestCookies = request.getCookies();
		Cookie authCookie= null;
		Cookie refreshCookie = null;
		if(requestCookies!=null){
		for(Cookie cookie : requestCookies){
			
			if(cookie.getName().equals("Authorization")){
				authCookie=cookie;
			}
			else if(cookie.getName().equals("refresh_token")){
				refreshCookie=cookie;
			}
		}
		
		}
		
	if(authCookie!=null&&authCookie.getMaxAge()<System.currentTimeMillis()){
		if(refreshCookie!=null&&refreshCookie.getMaxAge()>System.currentTimeMillis()){
			OAuth2AccessToken accessTokenFromRefreshToken =requestNewToken(refreshCookie.getValue());
		}
	}
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

	private OAuth2AccessToken requestNewToken(String value) {
	//	OAuth2AccessToken accessToken = restTemplate.postForObject(REFRESH_TOKEN_ENDPOINT, value, OAuth2AccessToken.class);
		return null;
	}

	
	

}
