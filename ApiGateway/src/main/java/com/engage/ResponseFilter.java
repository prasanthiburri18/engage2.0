package com.engage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class ResponseFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(ResponseFilter.class);

	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 7;
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

		ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
		log.info("Request scheme http/https: " + request.getScheme());
		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

		String requestUrl = request.getRequestURI();
		
		HttpServletResponse httpResponse = ctx.getResponse();
		
		if (requestUrl.contains(OAUTH2_TOKEN_URL)) {

			String value = "oauth2 cookie";
			Cookie cookie = new Cookie("AuthorizationToken", value);
			
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			httpResponse.addCookie(cookie);
		log.info(ctx.getResponseBody().toString());
		
		}

		String value = "Some cookie";
		Cookie cookie = new Cookie("Test ", value);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		cookie.setMaxAge(1000000);
		httpResponse.addCookie(cookie);
		return null;
	}

}