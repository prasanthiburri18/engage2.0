package com.engage.handler;

import org.jboss.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(RestExceptionHandler.class);

	/**
	 * Status 412 will be sent client application where user logout code is
	 * written
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(
			AccessDeniedException ex, WebRequest request) {
		WebRequest req =  request;
	
		LOGGER.info("Handling "+ex.getClass().getName());
		if(req.getUserPrincipal()!=null){
			
			LOGGER.info("Invalid access by "+req.getUserPrincipal().getName() );
			LOGGER.error("Invalid access by "+req.getUserPrincipal().getName()+" on "+req.getContextPath());
		}
		else{
			LOGGER.error("Invalid access of "+req.getContextPath());
		}
		
		return handleExceptionInternal(ex, "Unauthorized access", new HttpHeaders(),
				HttpStatus.PRECONDITION_FAILED, request);
	}
	
	
	@Override
	//@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object>  handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		WebRequest req =  request;
	
		LOGGER.info("Handling "+ex.getClass().getName());
		if(req.getUserPrincipal()!=null){
			
			LOGGER.info("Invalid access by "+req.getUserPrincipal().getName() );
			LOGGER.error("Invalid access by "+req.getUserPrincipal().getName()+" on "+req.getContextPath());
		}
		else{
			LOGGER.error("Invalid access of "+req.getContextPath());
		}
		
		return handleExceptionInternal(ex, "Invalid Request Format", new HttpHeaders(),
				HttpStatus.PRECONDITION_FAILED, request);
	}


}
