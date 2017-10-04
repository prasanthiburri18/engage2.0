package com.engage.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	/**
	 * Status 412 will be sent client application where user logout code is
	 * written
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(
			AccessDeniedException ex, WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
				HttpStatus.PRECONDITION_FAILED, request);
	}

}
