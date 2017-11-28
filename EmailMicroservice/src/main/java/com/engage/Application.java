package com.engage;

import java.util.Properties;

import org.slf4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * <p>
 * Email Microservice Spring Boot Application. Loading mail properties from
 * mail.properties.
 * </p>
 * 
 * @author mindtechlabs
 *
 */
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration

@EnableWebMvc
@PropertySource("classpath:mail.properties")
public class Application extends SpringBootServletInitializer {
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Application.class);

	@Value("${mail.protocol}")
	private String protocol;
	@Value("${mail.host}")
	private String host;
	@Value("${mail.port}")
	private int port;
	@Value("${mail.smtp.auth}")
	private boolean auth;
	@Value("${mail.smtp.starttls.enable}")
	private boolean starttls;
	@Value("${mail.from}")
	private String from;
	// Comment @Value annotation when deployed to other environment except local
	// @Value("${mail.username}")
	private String username;
	// Comment @Value annotation when deployed to other environment except local
	// @Value("${mail.password}")
	private String password;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Profile("dev")
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties mailProperties = new Properties();
		// mailProperties.put("mail.smtp.auth", auth);
		// mailProperties.put("mail.smtp.starttls.enable", starttls);
		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		/***
		 * Used when username and password configured as System properties in
		 * tomcat uncomment @Value annotation and edit mail properties of
		 * username and password for local(eclipse) setup
		 */
		this.username = System.getProperty("awsSesUsername");
		this.password = System.getProperty("awsSesPassword");
		LOGGER.info("Username and Password " + username + "  " + password);
		if (username == null || password == null || username == "" || password == "") {
			throw new BeanCreationException("Ses username/password is not configured as system properties");
		}

		mailSender.setUsername(username);
		mailSender.setPassword(password);
		return mailSender;
	}

	/**
	 * Java Mail sender bean for local profile
	 * 
	 * @return
	 */
	@Bean
	@Profile("local")
	public JavaMailSender javaMailSender2() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties mailProperties = new Properties();
		// mailProperties.put("mail.smtp.auth", auth);
		// mailProperties.put("mail.smtp.starttls.enable", starttls);
		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		/***
		 * Used when username and password configured as System properties in
		 * tomcat uncomment @Value annotation and edit mail properties of
		 * username and password for local(eclipse) setup
		 */

		mailSender.setUsername(username);
		mailSender.setPassword(password);
		return mailSender;
	}
}