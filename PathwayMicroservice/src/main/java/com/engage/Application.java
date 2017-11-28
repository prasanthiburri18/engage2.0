package com.engage;


import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

 
@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableWebMvc
public class Application extends SpringBootServletInitializer{

	@PostConstruct
	  void started() {
	    TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
	  }
	/**
	 * Singleton instance of {@link ObjectMapper}.
	 * This is used to convert map / entity to Dto.
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
		
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*")
					
					.allowedMethods("PUT", "DELETE","POST","GET")
					.allowedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers")
					.allowCredentials(false).maxAge(3600);
			}
        };
    }
	
	
	/**
	 * Validator to check Javax and Hibernate validations
	 * @return
	 */
	@Bean
	public Validator getValidator(){
		return  Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public static void main(String[] args) {
        SpringApplication.run(applicationClass, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
   
    private static Class<Application> applicationClass = Application.class;
}


@RestController
class GreetingController {

    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

}