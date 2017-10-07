package com.engage.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.engage.dao.UserDao;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {
	/**
	 * To get orgId based on email
	 */
	@Autowired
	private UserDao userDao;
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		User authenticatedUser = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();
        com.engage.model.User userFromDB = userDao.getById(authenticatedUser.getUsername());
        additionalInfo.put("organizationId", userFromDB.getOrgid());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
  		return accessToken;
		
		
	}

}
