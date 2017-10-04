package com.engage;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.engage.util.AdvancedEncryptionStandard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;


@Component
public class ResponseFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper = new ObjectMapper();
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
        		Date date = new Date();
        	response.setHeader("X-CSRF-TOKEN", AdvancedEncryptionStandard.encrypt(date.toString()));
        	}
      
/*            String responseBody = IOUtils.toString(is, "UTF-8");
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

            }*/
            ctx.setResponse(response);

        } catch (final IOException e) {
            logger.error("Error occured in zuul post filter", e);
        } catch (Exception e) {
		}
        return null;
    }



}
