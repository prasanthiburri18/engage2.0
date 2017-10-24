/**
 * 
 */
package com.engage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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

	@Autowired
	public ClientDetailsService clientDetailsService;
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http// .authenticationProvider(getAuthenticationProvider())
		.authorizeRequests()
		.antMatchers("/api/v1/**").permitAll()
		//.authenticated()
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
		converter.setSigningKey("secretkey");
		return converter;
	}

	@Bean
	@Primary
	public ResourceServerTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setClientDetailsService(clientDetailsService);
		return defaultTokenServices;
	}

}
