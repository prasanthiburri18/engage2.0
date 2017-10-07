package com.engage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.engage.exception.InvalidTokenException;
import com.engage.util.AdvancedEncryptionStandard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;


@Component
@Order(value = 6)
public class ResponseFilter extends GenericFilterBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String INIT_TOKEN_PATH ="/api/v1/initToken";
 /*   private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 4;
    }

    @Override
    public String filterType() {
        return "post";
    }
    
    
    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        logger.info("in zuul post filter " + ctx.getRequest().getRequestURI());

        try {
        	HttpServletRequest request = ctx.getRequest();
        	HttpServletResponse response = ctx.getResponse();
        	if(request.getUserPrincipal()!=null){
        		logger.info("Adding Token in response");
        		Date date = new Date();
        	response.setHeader("X-CSRF-TOKEN", AdvancedEncryptionStandard.encrypt(date.toString()));
        	}
      
            String responseBody = IOUtils.toString(is, "UTF-8");
            if (responseBody.contains("refresh_token")) {
                final Map<String, Object> responseMap = mapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {
                });
                final String refreshToken = responseMap.get("refresh_token").toString();
                responseMap.remove("refresh_token");
                responseBody = mapper.writeValueAsString(responseMap);

                final Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                // cookie.setSecure(true);
                cookie.setPath(ctx.getRequest().getContextPath() + "/oauth/token");
                cookie.setMaxAge(2592000); // 30 days
                ctx.getResponse().addCookie(cookie);
                logger.info("refresh token = " + refreshToken);

            }
            ctx.setResponse(response);

        } catch (final IOException e) {
            logger.error("Error occured in zuul post filter", e);
        } catch (Exception e) {
		}
        return null;
    }*/

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
    	HttpServletResponse res = (HttpServletResponse) response;
    	
    	if(req.getContextPath()==INIT_TOKEN_PATH ){
    		logger.info("Init Token added");
    		Date date = new Date();
    		try{
    		res.setHeader("X-CSRF-TOKEN", AdvancedEncryptionStandard.encrypt(date.toString()));
    		}
    		catch(Exception e){
    			res.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
    		}
    		}
    	else{
    		String token = req.getHeader("X-CSRF-TOKEN");
    		if(req.getHeader("X-CSRF-TOKEN")!=null){
    			try {
					validateToken(token);
				} catch (InvalidTokenException e) {
					
			
				} catch (Exception e) {
					logger.info("Decryption error");
				}
    		}
    	}
    	if(req.getContextPath().contains("/api/v1")){
    		logger.info("Adding Token in response");
    		
    		try {
    			Date date = new Date();
				res.setHeader("X-CSRF-TOKEN", AdvancedEncryptionStandard.encrypt(date.toString()));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
    	}
		
    	
    	chain.doFilter(request, response);
	}

	private boolean validateToken(String token) throws Exception {
	String dateString = AdvancedEncryptionStandard.decrypt(token);
	
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
	
	return false;
		
	}



}
