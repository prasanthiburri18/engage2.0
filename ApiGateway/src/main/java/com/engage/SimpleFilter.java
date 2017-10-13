package com.engage;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 2;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();

		HttpServletRequest request = ctx.getRequest();
		log.info("Http Version " + request.getProtocol());

		ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
		log.info("Request scheme http/https: " + request.getScheme());
		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

		return null;
	}

}