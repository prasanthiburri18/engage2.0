package com.engage;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;
/**
 * All the urls with /api/v1/ should have auth header, which is validated in
 * JWT filter
 * 
 * <h2>This is deprecated, {@link CookieAuthenticationFilter} uses replaces the logic used by this bean.
 *  Logic for getting claims from JWT is removed.</h2>
 * <code>Reason: Spring security Oauth2 with JWT implementation.</code>
 * @author mindtechlabs
 *
 */
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
    	  final HttpServletRequest request = (HttpServletRequest) req;

        final String authHeader = request.getHeader("Authorization");
   
        //Write logic in interceptor
  /*      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }
  */
      
        chain.doFilter(req, res);
    }

}
