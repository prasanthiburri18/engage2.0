package com.engage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
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


@EnableZuulProxy
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
/**
* API Gateway Operations
* Applying JWT Filters (Json Web Token Process Implementation)
* @author  StartUP Labs
* @version 1.0
* 
*/
public class Application extends SpringBootServletInitializer{
	@Bean
    public FilterRegistrationBean jwtFilter() 
	{
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/users/api/v1/*");
        registrationBean.addUrlPatterns("/patient/api/v1/*");
        registrationBean.addUrlPatterns("/pathway/api/v1/*");
        registrationBean.addUrlPatterns("/scheduled/api/v1/*");
        return registrationBean;
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
		    SpringApplication.run(Application.class, args);
		  }
		  @Bean
		  public SimpleFilter simpleFilter() {
		    return new SimpleFilter();
		  }
}

@RestController
class GreetingController {
    @RequestMapping("/api/v1/{name}")
    String hello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
}