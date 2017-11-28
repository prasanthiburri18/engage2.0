/**
 * 
 */
package com.engage.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * <h3>Logout Implementation</h3>
 * <p>
 * As this is a rest api, session is not persisted. Logout is implemented by
 * setting maxAge of Auth and Refresh cookies to -1
 * </p>
 * 
 * @author mindtechlabs
 *
 */
@Controller
public class LogoutController {
	/**
	 * Logger Implementation
	 */
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LogoutController.class);
/**
 * <p>Logout endpoint.</p>
 * <p>Expiry of tokens passed in the request is set to -1 and sent back in response.
 * Browser handles and invalidates those cookies.</p>
 * @param request
 * @param response
 * @return
 */
	@RequestMapping(value = "/userlogout", method = RequestMethod.GET)
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie authCookie = new Cookie("Authorization", null);
		authCookie.setHttpOnly(true);
		authCookie.setMaxAge(-1);
		authCookie.setPath("/ApiGateway");
		authCookie.setVersion(1);
		authCookie.setSecure(true);

		log.info("request domain :" + request.getServerName());
		authCookie.setDomain(request.getServerName());
		Cookie refreshCookie = new Cookie("refresh_token", null);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setPath("/ApiGateway");
		refreshCookie.setVersion(1);
		refreshCookie.setSecure(true);
		refreshCookie.setMaxAge(-1);
		refreshCookie.setDomain(request.getServerName());
		log.info("Send back expired cookies");
		response.addCookie(authCookie);
		response.addCookie(refreshCookie);

		ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
		return responseEntity;
	}

}
