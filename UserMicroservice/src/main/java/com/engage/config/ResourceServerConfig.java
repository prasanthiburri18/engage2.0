/**
 * 
 */
package com.engage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * This is a special case. Since we had already configured token store and
 * access token converter in {@link AuthorizationServerConfig}. We are
 * autowiring those beans here in ResourceServerConfig. All other microservices
 * except this(UserMicroservice) must have tokenstore and AccesstokenConverter
 * beans.
 * 
 * @author mindtech-labs
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	/**
	 * This Bean is already defined in {@link AuthorizationServerConfig} with
	 * JwtTokenStore. And JwtTokenStore bean defined in
	 * {@link AuthorizationServerConfig} is configured with
	 * JwtAccessTokenConverter which is also defined in
	 * {@link AuthorizationServerConfig}
	 * 
	 */
	@Autowired
	//private DefaultTokenServices tokenServices;
	//This wires to DefaultTokenServices defined in AuthorizationServerConfig
	private ResourceServerTokenServices tokenServices;
	/**
	 * TokenServices configured with JwtTokenStore and JwtAccessTokenConverter
	 * is leveraged here.
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

		resources.tokenServices(tokenServices);
	}

	/*
	 * @Bean public TokenStore tokenStore() { return new
	 * JwtTokenStore(accessTokenConverter()); }
	 * 
	 * @Bean public JwtAccessTokenConverter accessTokenConverter() {
	 * JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	 * converter.setSigningKey("123"); return converter; }
	 * 
	 * @Bean
	 * 
	 * @Primary public DefaultTokenServices tokenServices() {
	 * DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
	 * defaultTokenServices.setTokenStore(tokenStore()); return
	 * defaultTokenServices; }
	 */
}
