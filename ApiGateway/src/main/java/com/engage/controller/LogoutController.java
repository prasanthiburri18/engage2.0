/**
 * 
 */
package com.engage.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author mindtechlabs
 *
 */
@Controller
public class LogoutController {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LogoutController.class);
	
	@RequestMapping(value="/api/v1/logout", method=RequestMethod.GET)
	public HttpServletResponse logout(HttpServletRequest request, HttpServletResponse response){
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
		response.addCookie(authCookie);
		response.addCookie(refreshCookie);

		
		return response;
	}
	
	
}
