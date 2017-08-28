package com.engage;


import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

 
@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
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