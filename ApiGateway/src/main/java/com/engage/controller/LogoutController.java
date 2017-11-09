/**
 * 
 */
package com.engage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mindtechlabs
 *
 */
@Controller
public class LogoutController {

	@RequestMapping
	public String logout(){
		
		return null;
	}
	
	
}
