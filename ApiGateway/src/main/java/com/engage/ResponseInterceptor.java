/**
 * 
 */
package com.engage;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @author mindtech-labs
 *
 */
@Component
public class ResponseInterceptor extends HandlerInterceptorAdapter {
	private static final String EXPIRES_IN="validitiy";
	private static final String BEARER ="Bearer";
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseInterceptor.class);
	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";
	@Value("${zuul.routes.users.url}")
	private  String userMicroserviceUrl;
	private static final String REFRESH_TOKEN_ENDPOINT = "http://localhost:8080/UserMicroService/oauth/token/";
	private static final String LOGOUT_URL = "/ApiGateway/userlogout";
	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		
		
		HttpServletResponse httpResponse = response;
		String requestUrl = request.getRequestURI();

		Cookie[] requestCookies = request.getCookies();
		Cookie authCookie = null;
		Cookie refreshCookie = null;
		if (requestCookies != null&&requestCookies.length>=2) {
			for (Cookie cookie : requestCookies) {

				if (cookie.getName().equals("Authorization")) {
					authCookie = cookie;
				} else if (cookie.getName().equals("refresh_token")) {
					refreshCookie = cookie;
				}
			}
			/*if (requestUrl.equals(LOGOUT_URL) && request.getCookies() != null && request.getCookies().length > 1) {

				authCookie = new Cookie("Authorization", null);
				authCookie.setHttpOnly(true);
				authCookie.setMaxAge(-1);
				authCookie.setPath("/ApiGateway");
				authCookie.setVersion(1);
				authCookie.setSecure(true);

				LOGGER.info("request domain :" + request.getServerName());
				authCookie.setDomain(request.getServerName());
				refreshCookie = new Cookie("refresh_token", null);
				refreshCookie.setHttpOnly(true);
				refreshCookie.setPath("/ApiGateway");
				refreshCookie.setVersion(1);
				refreshCookie.setSecure(true);
				refreshCookie.setMaxAge(-1);
				refreshCookie.setDomain(request.getServerName());
				httpResponse.addCookie(authCookie);
				httpResponse.addCookie(refreshCookie);
				return false;
			}
*/		
			if (!(authCookie.getValue() == null || refreshCookie.getValue() == null || authCookie.getValue() == ""
					|| refreshCookie.getValue() == "")) {

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
		boolean isAuthTokenValid = ((authTokenValidityInSeconds - System.currentTimeMillis())/1000) > 0;
		
		String[] refreshCookieSplit = refreshCookie.getValue().split("#");
		String refreshTokenValidityString = null;
		for(String str : refreshCookieSplit){
			if(str.contains(EXPIRES_IN))	
				refreshTokenValidityString=str.replaceAll(EXPIRES_IN, "");
		
					
		}
		long refreshTokenValidityInSeconds = Long.parseLong(refreshTokenValidityString);
		
		boolean isRefreshTokenValid = ((refreshTokenValidityInSeconds -System.currentTimeMillis()/1000))>0;
		if (!isAuthTokenValid) {
			if (isRefreshTokenValid) {
				OAuth2AccessToken aTFromRefreshToken = requestNewToken(refreshCookie.getValue());
				if (aTFromRefreshToken != null) {
					
					Cookie authCookie1 = new Cookie("Authorization", " Bearer"+" "+aTFromRefreshToken.getValue()+"#"+EXPIRES_IN+aTFromRefreshToken.getExpiration().getTime());
					authCookie1.setHttpOnly(true);
					authCookie1.setMaxAge((int)(aTFromRefreshToken.getExpiration().getTime() - System.currentTimeMillis())/1000);
					authCookie1.setPath("/ApiGateway");
					authCookie1.setVersion(1);
					authCookie1.setSecure(true);
					
					LOGGER.info("request domain :"+ request.getServerName());
					authCookie1.setDomain(request.getServerName());
					Cookie refreshCookie1 = new Cookie("refresh_token", aTFromRefreshToken.getRefreshToken().getValue()+"#"+EXPIRES_IN+aTFromRefreshToken.getExpiration().getTime());
					refreshCookie1.setHttpOnly(true);
					refreshCookie1.setPath("/ApiGateway");
					refreshCookie1.setVersion(1);
					refreshCookie1.setSecure(true);
					refreshCookie1.setDomain(request.getServerName());
					httpResponse.addCookie(authCookie1);
					httpResponse.addCookie(refreshCookie1);
					
					
					//Add cookies to response as well
				}
			}
		}
		
		}
		}

		return true;
	}

	private HttpHeaders createHeaders(String username, String password){
		   return new HttpHeaders() {{
		         String auth = username + ":" + password;
		         String encodedAuth = Base64.encode( 
		            auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set( "Authorization", authHeader );
		      }};
		}
	private OAuth2AccessToken requestNewToken(String value) throws URISyntaxException {
		String grant_type = "refresh_token";
		Map<String, String> refreshData = new HashMap<>();
		refreshData.put("grant_type", grant_type);
		refreshData.put("refresh_token", value);
		HttpHeaders basicAuth = createHeaders("users", "ak#ANhKLLBRADHEadklj*$");
	//	String clientNameClientPassword = "users" + ":" + "ak#ANhKLLBRADHEadklj*$";
		//String basicAuthHeader = Base64.encode(clientNameClientPassword.getBytes());
		//basicAuth.set("Authorization", "Basic " + basicAuthHeader);
		
		URI refreshTokenUri = new URI(userMicroserviceUrl+"/oauth/token/");
		RequestEntity<Map<String, String>> request = new RequestEntity<Map<String, String>>(refreshData, basicAuth,
				HttpMethod.POST, refreshTokenUri);
	
		

		restTemplate.exchange(request, OAuth2AccessToken.class);
		ResponseEntity<OAuth2AccessToken> accessTokenEntity = restTemplate.exchange(request, OAuth2AccessToken.class);
		// OAuth2AccessToken accessToken =
		// restTemplate.postForObject(REFRESH_TOKEN_ENDPOINT, refreshData,
		// OAuth2AccessToken.class);
		OAuth2AccessToken accessToken = accessTokenEntity.getBody();

		return accessToken;
	}

}
