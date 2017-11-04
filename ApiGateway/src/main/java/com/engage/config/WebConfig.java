/**
 * 
 */
package com.engage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.engage.ResponseInterceptor;

/**
 * @author mindtech-labs
 *
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Autowired
    private ResponseInterceptor responseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(responseInterceptor).addPathPatterns("/**");


    }
}
