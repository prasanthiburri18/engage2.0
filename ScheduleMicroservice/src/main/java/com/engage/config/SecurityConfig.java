package com.engage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
// @EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	/**
	 * Encoder for password hashing. Changing logRounds is not advisable
	 * 
	 * @param args
	 */

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/*@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		// Don't change 10 log rounds hashing hardcoded.
		final int logRounds = 10;
		return new BCryptPasswordEncoder(logRounds);
	}

	@Bean
	public AuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(getPasswordEncoder());
		authProvider.setUserDetailsService(userDetailsService);
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(getAuthenticationProvider());
	}*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("Overriding spring security.");

		http// .authenticationProvider(getAuthenticationProvider())
				.authorizeRequests().anyRequest().permitAll().and().csrf().disable().formLogin().disable().httpBasic()
				.disable();
		;
	}
	// @formatter:on

/*	@Bean
	public OAuth2RestTemplate restTemplate() {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(clientCredentialsResourceDetails());
		return restTemplate;
	}

	@Bean
	@ConfigurationProperties(prefix = "security.oauth2.client")
	public OAuth2ProtectedResourceDetails clientCredentialsResourceDetails() {
		return new ClientCredentialsResourceDetails();
	}

	@Bean(name = "ouath2RestTemplate")
	public OAuth2RestTemplate getOAuth2RestTemplate(OAuth2ProtectedResourceDetails ccrd) {
		AccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
		OAuth2ClientContext context = new DefaultOAuth2ClientContext(accessTokenRequest);
		logger.debug("Checking loading of security.oauth2.client properties------");
		logger.info("Initializing oauth2RestTemplate, client Id:" + ccrd.getClientId());
		logger.debug("Finished Checking loading of security.oauth2.client properties");

		return new OAuth2RestTemplate(ccrd, context);
	}

	@Bean
	public AccessTokenRequest getAccessTokenRequest() {
		return new DefaultAccessTokenRequest();

	}*/

}
