package com.engage.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 
 * @author mindtech-labs
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfig.class);
	/**
	 * This is defined in {@link SecurityConfig}
	 */
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	
	@Autowired
	public ClientDetailsService clientDetailsService;

	
	
	/**
	 * JwtTokenStore returns JwtToken
	 * 
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/**
	 * Access Token converter. Using same symmetric signing key which is used in
	 * Engage1.0
	 * 
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("secretkey");
		return converter;
	}

	/**
	 * This is common for Resource and Authorization Server configuration in
	 * UserMicroservice
	 * 
	 * @return
	 */
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setClientDetailsService(clientDetailsService);
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	/**
	 * Token endpoint configuration. Wire authManagner, JwtTokenStore and AccessTokenConverter
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		  TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		    tokenEnhancerChain.setTokenEnhancers(
		      Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		 logger.info("Configuring auth server endpoints: tokenStore, tokenEnhancer and clientDetailsService");   
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancerChain).setClientDetailsService(clientDetailsService);;
	}
/**
 * Security on token endpoints
 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
				
		security.tokenKeyAccess("permitAll()").checkTokenAccess(
				"isAuthenticated()");
	}

	/**
	 * Client details of users, and all other microservices
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		logger.info("Configuring In memory clients");
		clients.inMemory()
		.withClient("users")
		//don't change this password
		.secret("ak#ANhKLLBRADHEadklj*$")
		.authorizedGrantTypes("password", "refresh_token")
		.scopes("client_app")
		.accessTokenValiditySeconds(86400).and()
		.withClient("usermicroservice")
		.secret("Password")
		.authorizedGrantTypes("client_credentials")
		.scopes("usermicroservice", "patientmicroservice","schedulemicroservice","pathwaymicroservice")
		.and()
		.withClient("patientmicroservice")
		.secret("Password")
		.authorizedGrantTypes("client_credentials")
		.scopes("usermicroservice", "patientmicroservice","schedulemicroservice","pathwaymicroservice")
		.and()
		.withClient("pathwaymicroservice")
		.secret("Password")
		.authorizedGrantTypes("client_credentials")
		.scopes("usermicroservice", "patientmicroservice","schedulemicroservice","pathwaymicroservice")
		.and()
		.withClient("schedulemicroservice")
		.secret("Password")
		.authorizedGrantTypes("client_credentials")
		.scopes("usermicroservice", "patientmicroservice","schedulemicroservice","pathwaymicroservice")
		.and()
		.withClient("apigateway")
		.secret("Password")
		.authorizedGrantTypes("client_credentials","password")
		.scopes("usermicroservice", "patientmicroservice","schedulemicroservice","pathwaymicroservice")
		;
		
	}

	@Bean
	public TokenEnhancer tokenEnhancer(){
		return new CustomTokenEnhancer();
	}
	
}
