package com.engage.config;

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
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
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
	/**
	 * This is defined in {@link SecurityConfig}
	 */
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	
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
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// TODO Auto-generated method stub
		/*security.tokenKeyAccess("permitAll()").checkTokenAccess(
				"isAuthenticated()");
		*/
		
		security.tokenKeyAccess("permitAll()").checkTokenAccess(
				"isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("users")
		.secret("Password")
		.authorizedGrantTypes("client_credentials", "password", "refresh_token")
		.scopes("usermicroservice", "patientmicroservice","schedulemicroservice","pathwaymicroservice")
		.authorities("admin","user")
		.accessTokenValiditySeconds(86400).and()
		.withClient("mailmicroservice")
		.secret("Password")
		.authorizedGrantTypes("client_credentials", "password", "refresh_token")
		.scopes("usermicroservice", "patientmicroservice","schedulemicroservice","pathwaymicroservice")
		.authorities("admin","user")
		.accessTokenValiditySeconds(86400);
	}

}
