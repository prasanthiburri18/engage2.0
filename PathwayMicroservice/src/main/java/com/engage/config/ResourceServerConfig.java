/**
 * 
 */
package com.engage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/**
 * <p>
 * Resource Server configuration for Pathway Microservice
 * </p>
 * <p>
 * Client Details are configured in AuthorizationServerConfig in User
 * microservice
 * </p>
 * 
 * @author mindtech-labs
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	/**
	 * Symmetric signing key constant
	 */
	private static final String SIGNING_KEY = "secretkey";
	/**
	 * <p> {@link HttpSecurity} configuration </p>
	 * <p>This overrides {@link HttpSecurity} configured by {@link SecurityConfig}.</p>
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/api/v1/**").permitAll()
		.anyRequest().permitAll()
		.and().csrf().disable().formLogin().disable().httpBasic()
				.disable();
		;
	}

	/**
	 * TokenServices configured with JwtTokenStore and JwtAccessTokenConverter
	 * is leveraged here.
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources)
			throws Exception {

		resources.tokenServices(tokenServices());
	}
	/**
	 * <p>JwT token store</p>
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/**
	 * Symmetric key is used here
	 * 
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(SIGNING_KEY);
		return converter;
	}

	@Bean
	@Primary
	public ResourceServerTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		
		return defaultTokenServices;
	}

}
