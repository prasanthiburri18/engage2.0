 package com.engage;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.mysql.fabric.xmlrpc.base.Data;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class SimpleFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";
	private static final String EXPIRES_IN="validitiy";
	private static final String BEARER ="Bearer";
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 2;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
	

		HttpServletRequest request = ctx.getRequest();
		log.info("Http Version " + request.getProtocol());
		
		log.info("Request scheme http/https: " + request.getScheme());
		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

		String requestUrl = request.getRequestURI();
		String basicAuthHeader = request.getHeader("Authorization");

		Cookie[] requestCookies = request.getCookies();
		Cookie authCookie= null;
		Cookie refreshCookie = null;
		if(requestCookies!=null&&!(basicAuthHeader!=null&&basicAuthHeader.contains("Basic"))){
		for(Cookie cookie : requestCookies){
			
			if(cookie.getName().equals("Authorization")){
				authCookie=cookie;
			}
			else if(cookie.getName().equals("refresh_token")){
				refreshCookie=cookie;
			}
		}
		
		String[] authCookieSplit = authCookie.getValue().split("#");
		String authHeader=null;
		String authValidityString=null;
		for(String str : authCookieSplit){
			if(str.contains(EXPIRES_IN))	
				authValidityString=str.replaceAll(EXPIRES_IN, "");
			else if(str.contains(BEARER))
				authHeader=str;
					
		}
		long authTokenValidityInSeconds = Long.parseLong(authValidityString);
		boolean isAuthTokenValid = (authTokenValidityInSeconds - System.currentTimeMillis()/1000) > 0;
		if(authHeader!=null && isAuthTokenValid){
			//if authtoken still exists
		ctx.addZuulRequestHeader("Authorization", authHeader);
		}
		
		}
		else{
			ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
		}
		
		return null;
	}

	private OAuth2AccessToken requestNewToken(String value) {

		return null;
	}

}