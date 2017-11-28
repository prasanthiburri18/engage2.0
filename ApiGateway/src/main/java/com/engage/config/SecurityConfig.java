package com.engage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import com.engage.Application;
/**
 * <b>Security Configuration for ApiGateway</b>
 * <p>Order is kept maximum, in order {@link ResourceServerConfig} to override {@link HttpSecurity}</p>
 * @author mindtechlabs
 *
 */
@Configuration
// @EnableWebSecurity
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

		http

				.authorizeRequests()

				.anyRequest().permitAll()

				.and().csrf().disable().formLogin().disable().httpBasic().disable();
		;
	}

	@Bean
	@ConfigurationProperties(prefix = "security.oauth2.client")
	public OAuth2ProtectedResourceDetails clientCredentialsResourceDetails() {
		return new ClientCredentialsResourceDetails();
	}

	/**
	 * <p>Oauth2RestTemplate with Client Context and Details. A default {@link OAuth2ClientContext} is created 
	 * when {@link EnableOAuth2Client} annotation is used in {@link Application}.
	 * </p>
	 * @param OAuth2ClientContext and OAuth2ProtectedResourceDetails
	 * @return
	 */
	@Bean(name = "oauth2RestTemplate")
	public OAuth2RestTemplate getOAuth2RestTemplate(OAuth2ClientContext context, OAuth2ProtectedResourceDetails ccrd) {

		logger.debug("Checking loading of security.oauth2.client properties------");
		logger.info("Initializing oauth2RestTemplate, client Id:" + ccrd.getClientId());
		logger.debug("Finished Checking loading of security.oauth2.client properties");

		return new OAuth2RestTemplate(ccrd, context);
	}

}
