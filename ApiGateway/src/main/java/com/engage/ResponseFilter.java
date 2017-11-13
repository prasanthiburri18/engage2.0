package com.engage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendResponseFilter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.context.RequestContext;

public class ResponseFilter extends SendResponseFilter {

	private static Logger log = LoggerFactory.getLogger(ResponseFilter.class);

	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";

	private static final String BEARER ="Bearer";
	private static final String EXPIRES_IN="validitiy";

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();

		HttpServletRequest request = ctx.getRequest();
		log.info("Http Version " + request.getProtocol());

	
		log.info("Request scheme http/https: " + request.getScheme());
		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		log.info("request domain :"+ request.getServerName());
		String requestUrl = request.getRequestURI();

		HttpServletResponse httpResponse = ctx.getResponse();
		if(request.getCookies()!=null){
		Cookie[] requestCookies = request.getCookies();
		for(Cookie cookie : requestCookies){
			log.info(cookie.getValue());
		}}
		if (requestUrl.contains(OAUTH2_TOKEN_URL) && ctx.getResponseStatusCode() == 200) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				boolean nullCheck = ctx.getResponseBody() != null;
				log.info("Is Response Body null? " + nullCheck);

				nullCheck = ctx.getResponseDataStream() != null;
				log.info("Is Response data stream null? " + nullCheck);
				OAuth2AccessToken token = mapper.readValue(ctx.getResponseDataStream(), OAuth2AccessToken.class);
				log.info(token.getValue());
				byte[] byteArray = mapper.writeValueAsBytes(token);
				
				InputStream responseDataStream = new ByteArrayInputStream(byteArray);
				ctx.setResponseDataStream(responseDataStream);
				Cookie authCookie = new Cookie("Authorization", " Bearer"+" "+token.getValue()+"#"+EXPIRES_IN+token.getExpiration().getTime());
				authCookie.setHttpOnly(true);
				authCookie.setMaxAge((int)(token.getExpiration().getTime() - System.currentTimeMillis())/1000);
				authCookie.setPath("/ApiGateway");
				authCookie.setVersion(1);
				authCookie.setSecure(true);
				
				log.info("request domain :"+ request.getServerName());
				authCookie.setDomain(request.getServerName());
				Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken().getValue()+"#"+EXPIRES_IN+token.getExpiration().getTime());
				refreshCookie.setHttpOnly(true);
				refreshCookie.setPath("/ApiGateway");
				refreshCookie.setVersion(1);
			refreshCookie.setSecure(true);
				refreshCookie.setDomain(request.getServerName());
				httpResponse.addCookie(authCookie);
				httpResponse.addCookie(refreshCookie);
				
			} catch (JSONException | IOException e) {
					log.error("Error while setting authorization cookies. Exception message: "+e.getMessage());
				e.printStackTrace();
			}

		

		} 
		return null;
	}

}