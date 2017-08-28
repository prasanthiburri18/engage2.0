package com.engage;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration

@EnableWebMvc
@PropertySource("classpath:mail.properties")
public class Application extends SpringBootServletInitializer {
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
	    @Value("${mail.username}")
	    private String username;
	    @Value("${mail.password}")
	    private String password;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	 @Bean
	    public JavaMailSender javaMailSender() {
	        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	        Properties mailProperties = new Properties();
	        mailProperties.put("mail.smtp.auth", auth);
	        mailProperties.put("mail.smtp.starttls.enable", starttls);
	        mailSender.setJavaMailProperties(mailProperties);
	        mailSender.setHost(host);
	        mailSender.setPort(port);
	        mailSender.setProtocol(protocol);
	        mailSender.setUsername(username);
	        mailSender.setPassword(password);
	        return mailSender;
	    }
}