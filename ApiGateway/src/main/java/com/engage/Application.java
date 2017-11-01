package com.engage;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableZuulProxy
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
/**
 * API Gateway Operations Applying JWT Filters (Json Web Token Process
 * Implementation)
 * 
 * @author StartUP Labs
 * @version 1.0
 * 
 */
public class Application extends SpringBootServletInitializer {
	
	//Logger implementation
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	/**
	 * All the urls with /api/v1/ should have auth header, which is validated in JWT filter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/users/api/v1/*");
		registrationBean.addUrlPatterns("/patient/api/v1/*");
		registrationBean.addUrlPatterns("/pathway/api/v1/*");
		registrationBean.addUrlPatterns("/scheduled/api/v1/*");
		LOGGER.info("Registering JWT filter");
		return registrationBean;
	}


	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*")
						.allowedMethods("PUT", "DELETE", "POST", "GET").allowedHeaders("Access-Control-Allow-Origin",
								"Access-Control-Allow-Credentials", "Access-Control-Allow-Headers")
						.allowCredentials(false).maxAge(3600);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public SimpleFilter simpleFilter() {
		LOGGER.info("Creating Simple Filter Bean");
		return new SimpleFilter();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer(){
		
		return new PropertySourcesPlaceholderConfigurer();
	}
}

@RestController
class GreetingController {
	private static Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

	@RequestMapping("/api/v1/{name}")
	String hello(@PathVariable String name, HttpServletRequest req) {
		LOGGER.info("**" + req.getContextPath() + "**");
		LOGGER.info("this is in greeting controller :" + name);
		//LOGGER.error("this is in greeting controller :" + name);
		LOGGER.info(req.getUserPrincipal().toString());
		return "Hello, " + req.getUserPrincipal().toString() + "!";
	}
	
	@RequestMapping("/api/v1/initToken")
	public ResponseEntity<String> getInitToken(HttpServletRequest req) {
		LOGGER.info("**" + req.getContextPath() + "**");
		LOGGER.info("this is in greeting controller :");
		
		//LOGGER.error("this is in greeting controller :" + name);
		
		return new ResponseEntity<String>("_csrf token", HttpStatus.OK);
	}
}