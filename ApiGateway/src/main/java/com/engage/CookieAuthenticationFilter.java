/**
 * 
 */
package com.engage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @author mindtech-labs
 *
 */

@Component
@Order(value = 2)
public class CookieAuthenticationFilter extends GenericFilterBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CookieAuthenticationFilter.class);
	private static final String REFRESH_TOKEN_ENDPOINT = "http://192.168.0.113:8081/oauth/token?grant_type=refresh_token";
	private static final String LOGOUT_URL = "/ApiGateway/userlogout";
	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";
	private static final String EXPIRES_IN="validitiy";
	private static final String BEARER ="Bearer";
	@Value("${zuul.routes.users.url}")
	private String userMicroserviceUrl;
	//@Autowired
	private RestTemplate restTemplate= new RestTemplate();
	/**
	 * 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
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
				try {
					checkForAuthTokenExpiry(req,res);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				chain.doFilter(request, response);
			}
		}
		else{
			LOGGER.info("Protected Url access without credentials: "+req.getRequestURI()+" "+req.getUserPrincipal());
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
		}
	
	}


	private void checkForAuthTokenExpiry(HttpServletRequest req, HttpServletResponse response) throws URISyntaxException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
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
				//	authCookie1.setSecure(true);
					
					LOGGER.info("request domain :"+ request.getServerName());
					authCookie1.setDomain(request.getServerName());
					Cookie refreshCookie1 = new Cookie("refresh_token", aTFromRefreshToken.getRefreshToken().getValue()+"#"+EXPIRES_IN+aTFromRefreshToken.getExpiration().getTime());
					refreshCookie1.setHttpOnly(true);
					refreshCookie1.setPath("/ApiGateway");
					refreshCookie1.setVersion(1);
					//refreshCookie1.setSecure(true);
					refreshCookie1.setDomain(request.getServerName());
					httpResponse.addCookie(authCookie1);
					httpResponse.addCookie(refreshCookie1);
					
					
					//Add cookies to response as well
				}
			}
		}
		
		}
		}

	}

	private HttpHeaders createHeaders(String username, String password){
		   return new HttpHeaders() {{
		         String auth = username + ":" + password;
		         String encodedAuth = Base64.encode( 
		            auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set( "Authorization", authHeader );
		         add("Authorization", authHeader);
		   }};
		}
	private OAuth2AccessToken requestNewToken(String value) throws URISyntaxException {
		String grant_type = "refresh_token";
		Map<String, String> refreshData = new HashMap<>();
		refreshData.put("grant_type", grant_type);
		refreshData.put("refresh_token", value);
		HttpHeaders basicAuth = createHeaders("users", "ak#ANhKLLBRADHEadklj*$");
		basicAuth.setContentType(MediaType.APPLICATION_JSON);
	//	String clientNameClientPassword = "users" + ":" + "ak#ANhKLLBRADHEadklj*$";
		//String basicAuthHeader = Base64.encode(clientNameClientPassword.getBytes());
		//basicAuth.set("Authorization", "Basic " + basicAuthHeader);
		
		//URI refreshTokenUri = new URI(userMicroserviceUrl+"/oauth/token/");
		URI refreshTokenUri = new URI(REFRESH_TOKEN_ENDPOINT);
	/*	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		LOGGER.info(" Authentication "+authentication.toString() );
	*/	RequestEntity<Map<String, String>> request = new RequestEntity<Map<String, String>>(refreshData, basicAuth,			HttpMethod.POST, refreshTokenUri);
		//HttpEntity<Map<String, String>> request = new HttpEntity<Map<String,String>>(refreshData, basicAuth);
		//ResponseEntity<OAuth2AccessToken> accessTokenEntity1=		 restTemplate.exchange(refreshTokenUri, HttpMethod.POST, request, OAuth2AccessToken.class);
		//ResponseEntity<OAuth2AccessToken> accessTokenEntity1= restTemplate.postForEntity(REFRESH_TOKEN_ENDPOINT, request, OAuth2AccessToken.class);
		//restTemplate.exchange(request, OAuth2AccessToken.class);
		ResponseEntity<OAuth2AccessToken> accessTokenEntity1 = restTemplate.exchange(request, OAuth2AccessToken.class);
		// OAuth2AccessToken accessToken =
		// restTemplate.postForObject(REFRESH_TOKEN_ENDPOINT, refreshData,
		// OAuth2AccessToken.class);
		OAuth2AccessToken accessToken = accessTokenEntity1.getBody();

		return accessToken;
	}

}
