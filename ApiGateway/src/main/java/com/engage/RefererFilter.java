package com.engage;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.engage.dao.IRefererDao;
import com.engage.model.Referer;

/**
 * <p>{@link RefererFilter} filters referer header present in request.</p>
 * Predefined list of valid referers are stored in database. 
 * @author mindtech-labs
 *
 */
@Component
@Order(value = 4)
public class RefererFilter extends GenericFilterBean {
	/**
	 * Logger implementation
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RefererFilter.class);

	private static final String USER_REPLY_URI="/ApiGateway/users/userreply";
	/**
	 * Autowired instance of {@link IRefererDao}
	 */
	@Autowired
	private IRefererDao refererDao;



	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String requestUri = req.getRequestURI();
		final String referer = req.getHeader("Referer");
		
		
		List<Referer> referers = refererDao.findAll();

		
		if ((referer == null
				|| referers.stream().noneMatch(
						r -> referer.contains(r.getReferer())))&&!requestUri.equalsIgnoreCase(USER_REPLY_URI)) {
			((HttpServletResponse) response)
					.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOGGER.info("Invalid referer header passed to access "
					+ req.getRequestURI());
			LOGGER.error("Invalid referer: " + referer);
		} else {
			LOGGER.info("Referer " + referer + " is valid");
			chain.doFilter(request, response);
		}
	}

}
