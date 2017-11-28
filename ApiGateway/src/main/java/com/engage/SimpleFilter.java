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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * This Zuul filter is <b>"pre"</b> type. Strips Auth & Refresh Token from
 * Cookie and adds a ZuulRequestHeader Authorization from login request to
 * response cookies.
 * 
 * @author mindtechlabs
 *
 */
@Component
public class SimpleFilter extends ZuulFilter {

	private static Logger LOGGER = LoggerFactory.getLogger(SimpleFilter.class);

	private static final String OAUTH2_TOKEN_URL = "/ApiGateway/users/oauth/token";
	private static final String EXPIRES_IN = "validitiy";
	private static final String BEARER = "Bearer";

	private static final String LOGOUT_URL = "/ApiGateway/userlogout";
	@Value("${zuul.routes.users.url}")
	private String userMicroserviceUrl;
	@Autowired

	private RestTemplate restTemplate;

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
		HttpServletResponse response = ctx.getResponse();
		LOGGER.info("Http Version " + request.getProtocol());

		LOGGER.info("Request scheme http/https: " + request.getScheme());
		LOGGER.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

		String requestUrl = request.getRequestURI();
		String basicAuthHeader = request.getHeader("Authorization");

		Cookie[] requestCookies = request.getCookies();
		Cookie authCookie = null;
		Cookie refreshCookie = null;
		if (requestCookies != null && !(basicAuthHeader != null && basicAuthHeader.contains("Basic"))) {
			for (Cookie cookie : requestCookies) {

				if (cookie.getName().equals("Authorization")) {
					LOGGER.info("Loading Auth Cookie");
					authCookie = cookie;
				} else if (cookie.getName().equals("refresh_token")) {
					LOGGER.info("Loading Refresh Token Cookie");
					refreshCookie = cookie;
				}
			}
			// Work around to validate expiry of authCookie. As cookies are not
			// persisted on server side.
			String[] authCookieSplit = authCookie.getValue().split("#");
			String authHeader = null;
			String authValidityString = null;
			for (String str : authCookieSplit) {
				if (str.contains(EXPIRES_IN))
					authValidityString = str.replaceAll(EXPIRES_IN, "");
				else if (str.contains(BEARER))
					authHeader = str;

			}
			if (authValidityString != null && !authValidityString.equals("")) {
				long authTokenValidityInSeconds = Long.parseLong(authValidityString);
				boolean isAuthTokenValid = (authTokenValidityInSeconds - System.currentTimeMillis() / 1000) > 0;
				if (authHeader != null && isAuthTokenValid) {
					// if authtoken still exists
					/*
					 * try { checkForAuthTokenExpiry(request, response); } catch
					 * (URISyntaxException e) {
					 * 
					 * }
					 */
					ctx.addZuulRequestHeader("Authorization", authHeader);
				}
			}
		} else {
			ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
		}

		return null;
	}

	/**
	 * <p>
	 * Method to verify Authorization Token Expiry.
	 * </p>
	 * 
	 * @param req
	 * @param response
	 * @throws URISyntaxException
	 */
	private void checkForAuthTokenExpiry(HttpServletRequest req, HttpServletResponse response)
			throws URISyntaxException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestUrl = request.getRequestURI();

		Cookie[] requestCookies = request.getCookies();
		Cookie authCookie = null;
		Cookie refreshCookie = null;
		if (requestCookies != null && requestCookies.length >= 2) {
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
				String authHeader = null;
				String authValidityString = null;
				for (String str : authCookieSplit) {
					if (str.contains(EXPIRES_IN))
						authValidityString = str.replaceAll(EXPIRES_IN, "");
					else if (str.contains(BEARER))
						authHeader = str;

				}
				long authTokenValidityInSeconds = Long.parseLong(authValidityString);
				boolean isAuthTokenValid = ((authTokenValidityInSeconds - System.currentTimeMillis()) / 1000) > 0;

				String[] refreshCookieSplit = refreshCookie.getValue().split("#");
				String refreshTokenValidityString = null;
				for (String str : refreshCookieSplit) {
					if (str.contains(EXPIRES_IN))
						refreshTokenValidityString = str.replaceAll(EXPIRES_IN, "");

				}
				long refreshTokenValidityInSeconds = Long.parseLong(refreshTokenValidityString);

				boolean isRefreshTokenValid = ((refreshTokenValidityInSeconds - System.currentTimeMillis() / 1000)) > 0;
				if (!isAuthTokenValid) {
					if (isRefreshTokenValid) {
						OAuth2AccessToken aTFromRefreshToken = requestNewToken(refreshCookie.getValue());
						if (aTFromRefreshToken != null) {

							Cookie authCookie1 = new Cookie("Authorization",
									" Bearer" + " " + aTFromRefreshToken.getValue() + "#" + EXPIRES_IN
											+ aTFromRefreshToken.getExpiration().getTime());
							authCookie1.setHttpOnly(true);
							authCookie1.setMaxAge(
									(int) (aTFromRefreshToken.getExpiration().getTime() - System.currentTimeMillis())
											/ 1000);
							authCookie1.setPath("/ApiGateway");
							authCookie1.setVersion(1);
							// authCookie1.setSecure(true);

							LOGGER.info("request domain :" + request.getServerName());
							authCookie1.setDomain(request.getServerName());
							Cookie refreshCookie1 = new Cookie("refresh_token",
									aTFromRefreshToken.getRefreshToken().getValue() + "#" + EXPIRES_IN
											+ aTFromRefreshToken.getExpiration().getTime());
							refreshCookie1.setHttpOnly(true);
							refreshCookie1.setPath("/ApiGateway");
							refreshCookie1.setVersion(1);
							// refreshCookie1.setSecure(true);
							refreshCookie1.setDomain(request.getServerName());
							httpResponse.addCookie(authCookie1);
							httpResponse.addCookie(refreshCookie1);

							// Add cookies to response as well
						}
					}
				}

			}
		}

	}

	private HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				String encodedAuth = Base64.encode(auth.getBytes(Charset.forName("UTF-8")));
				String authHeader = "Basic " + new String(encodedAuth);
				// set( "Authorization", authHeader );
				add("Authorization", authHeader);
			}
		};
	}

	private OAuth2AccessToken requestNewToken(String value) throws URISyntaxException {
		final String REFRESH_TOKEN_ENDPOINT = userMicroserviceUrl + "oauth/token?grant_type=refresh_token";
		String grant_type = "refresh_token";
		Map<String, String> refreshData = new HashMap<>();
		refreshData.put("grant_type", grant_type);
		refreshData.put("refresh_token", value);
		HttpHeaders basicAuth = createHeaders("users", "ak#ANhKLLBRADHEadklj*$");
		URI refreshTokenUri = new URI(REFRESH_TOKEN_ENDPOINT);

		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(refreshData, basicAuth);
		ResponseEntity<OAuth2AccessToken> accessTokenEntity1 = restTemplate.exchange(refreshTokenUri, HttpMethod.POST,
				request, OAuth2AccessToken.class);

		OAuth2AccessToken accessToken = accessTokenEntity1.getBody();

		return accessToken;
	}

}