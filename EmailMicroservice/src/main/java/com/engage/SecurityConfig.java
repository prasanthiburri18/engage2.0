package com.engage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
/**
 * <b>Security Configuration for ApiGateway</b>
 * <p>Order is kept maximum, in order {@link ResourceServerConfig} to override {@link HttpSecurity}</p>
 * @author mindtechlabs
 *
 */
@Configuration
//@EnableWebSecurity
@EnableOAuth2Client
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("Overriding spring security.");

		http// .authenticationProvider(getAuthenticationProvider())
				
		.authorizeRequests()
		//.antMatchers("/api/v1/**").authenticated()
		.anyRequest().permitAll()
				
				.and().csrf().disable().formLogin().disable().httpBasic()
				.disable();
		;
	}



}
