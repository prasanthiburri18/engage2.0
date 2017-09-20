package com.engage;


import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

 
@Configuration
@ComponentScan
@EnableAutoConfiguration
//For now excluding SecurityAutoConfigurtion.class. As security is not implement. Just BCryptEncoder is used
@SpringBootApplication//(exclude={SecurityAutoConfiguration.class})
@EnableWebMvc
public class Application extends SpringBootServletInitializer{
//	@Bean
//    public FilterRegistrationBean jwtFilter() {
//        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new JwtFilter());
//        registrationBean.addUrlPatterns("/api/*");
//
//        return registrationBean;
//    }
	@PostConstruct
	  void started() {
	    TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
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
	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name, HttpServletRequest request) {
    	String str = request.getUserPrincipal().toString();
    	LOGGER.info(str);
    	LOGGER.info("Testing error log in UserMicroservice");
      //LOGGER.error("Testing error log in UserMicroservice");
        return "Hello, " + name + "!";
    }

}