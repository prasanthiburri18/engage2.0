package com.engage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ActiveProfiles(profiles={"local","dev"})
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(org.springframework.web.servlet.config.annotation.ViewControllerRegistry registry) {
	registry.addRedirectViewController("/", "/swagger-ui.html")	;
	};
	
	@Override
	public void addResourceHandlers(
			org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations(
				"classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations(
				"classpath:/META-INF/resources/webjars/");
	}
}
