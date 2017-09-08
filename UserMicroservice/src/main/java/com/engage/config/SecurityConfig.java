package com.engage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
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
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("Overriding spring security.");

		http// .authenticationProvider(getAuthenticationProvider())
				.authorizeRequests().anyRequest().permitAll().and().csrf().disable().formLogin().and().httpBasic();
	}
	// @formatter:on
}
