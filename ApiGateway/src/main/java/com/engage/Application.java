package com.engage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.netflix.zuul.ZuulFilter;

@EnableZuulProxy
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
@EnableOAuth2Client
/**
 * <p>API Gateway Operations Applying JWT Filters (Json Web Token Process
 * Implementation)</p>
 * 
 * @author StartUP Labs
 * @version 1.0
 * 
 */
public class Application extends SpringBootServletInitializer {

	/**
	 * <p>Class Logger Implementation</p>
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	/**
	 * All the urls with /api/v1/ should have auth header, which is validated in
	 * JWT filter
	 * 
	 * <p>This is deprecated, {@link CookieAuthenticationFilter} uses replaces the logic used by this bean.
	 *  Logic for getting claims from JWT is removed.</p>
	 * <code>Reason: Spring security Oauth2 with JWT implementation.</code>
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
	/**
	 * <p>RestTemplate is autowired in any class where there is Microservice-Microservice communication.</p>
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate(){
	return	new RestTemplate();
	}
	
	/**
	 * This filter validates all the request with <b>/api/v1/</b> as request prefix in request url.
	 * Registering {@link CookieAuthenticationFilter} as a bean.  
	 * @return
	 */
	@Bean
	public FilterRegistrationBean cookieAuthenticationFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

		registrationBean.setFilter(new CookieAuthenticationFilter());
		registrationBean.addUrlPatterns("/users/api/v1/*");
		registrationBean.addUrlPatterns("/patient/api/v1/*");
		registrationBean.addUrlPatterns("/pathway/api/v1/*");
		registrationBean.addUrlPatterns("/scheduled/api/v1/*");
		LOGGER.info("Registering Cookie authentication filter");
		return registrationBean;
	}

	/**
	 * Cors Configuration
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*")
						.allowedMethods("PUT", "DELETE", "POST", "GET").allowedHeaders("Access-Control-Allow-Origin",
								"Access-Control-Allow-Credentials", "Access-Control-Allow-Headers")
						.allowCredentials(true).maxAge(3600);
			}
			

		};
	}

	public static void main(String[] args) {
		LOGGER.info("Starting ApiGateway...");
		SpringApplication.run(Application.class, args);
	}
	/**
	 * This registers Pre {@link ZuulFilter}
	 * @return
	 */
	@Bean
	public SimpleFilter simpleFilter() {
		LOGGER.info("Creating Simple Filter Bean");
		return new SimpleFilter();
	}
	
	/**
	 * This registers Post {@link ZuulFilter}
	 * @return
	 */
	@Bean
	public ResponseFilter responseFilter() {
		LOGGER.info("Creating Response Filter Bean");
		return new ResponseFilter();
	}
	/**
	 * <p>This static bean loads before configuration is loaded.
	 * Used to load, any property files (other than application.properties) 
	 * before creating any beans</p> 
	 * @return
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {

		return new PropertySourcesPlaceholderConfigurer();
	}
	
	/**
	 * Client details configuration {@link ClientCredentialsResourceDetails} for {@link OAuth2RestTemplate}.
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "security.oauth2.client")
	public OAuth2ProtectedResourceDetails clientCredentialsResourceDetails() {
		LOGGER.info("Loading Client Credentials of ApiGateway");
		return new ClientCredentialsResourceDetails();
	}

	/**
	 * Oauth2RestTemplate with Client Context and Details
	 * 
	 * @param ccrd
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
