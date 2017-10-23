package com.engage.service;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

public class CustomClientDetailsService extends ClientDetailsUserDetailsService {

	public CustomClientDetailsService(ClientDetailsService clientDetailsService) {
		super(clientDetailsService);
		
	}

}
