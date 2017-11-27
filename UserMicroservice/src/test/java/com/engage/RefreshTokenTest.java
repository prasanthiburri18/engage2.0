package com.engage;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class RefreshTokenTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenTest.class);
	private static final String REFRESH_TOKEN_ENDPOINT = "http://192.168.0.113:8081/oauth/token?grant_type=refresh_token";
	private static final String LOGOUT_URL = "/ApiGateway/userlogout";
	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";
	private static final String EXPIRES_IN="validitiy";
	private static final String BEARER ="Bearer";

	
	private RestTemplate restTemplate= new RestTemplate();

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

	@Test
	public void getAccessTokenTest() throws URISyntaxException{
		String rf = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmdhbml6YXRpb25JZCI6NywidXNlcl9uYW1lIjoicy5rcmlzaG5hQG1pbmR0ZWNobGFicy5jb20iLCJzY29wZSI6WyJjbGllbnRfYXBwIl0sImF0aSI6ImQyNTA2YTE2LTU4Y2UtNDcyZS1iM2ExLWIzNjVkNGU2YjNjZSIsImV4cCI6MTUxMTI0ODA1NSwiYXV0aG9yaXRpZXMiOlsiQSIsIjciXSwianRpIjoiM2U2M2VlOGMtMjI5YS00ZWM3LThlMmYtNmNlNmFlODNmMTYyIiwiY2xpZW50X2lkIjoidXNlcnMifQ.XKYYv16cUk6qRzcS0_ZkuJz1YCbWqSzrNh73uyujpFM";
		requestNewToken(rf);
		LOGGER.info(rf.toString());
	}
}
