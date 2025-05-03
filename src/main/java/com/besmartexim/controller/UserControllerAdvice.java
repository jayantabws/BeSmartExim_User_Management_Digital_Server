package com.besmartexim.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.besmartexim.dto.response.ErrorResponse;
import com.besmartexim.exception.ServiceException;

@ControllerAdvice
public class UserControllerAdvice extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(UserControllerAdvice.class);
	
	 	@ExceptionHandler(Exception.class)
	 	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	    public @ResponseBody ErrorResponse handleAllException(Exception ex, WebRequest request) {

	 		ErrorResponse errorResponse =new ErrorResponse();
	 		errorResponse.setTimestamp(LocalDateTime.now());
	 		errorResponse.setErrorMsg(ex.getMessage());
	 		String error= ex.toString();
	 		logger.info(error);
	 		if(error.length() > 200) {
	 			error = error.substring(0, 200);
	 		}
	 		errorResponse.setErrorDesc(error);
	 		
	        return errorResponse;
	    }
	 
	 	@ExceptionHandler(ServiceException.class)
		@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
		public @ResponseBody ErrorResponse handleResourceNotFound( ServiceException ex, HttpServletRequest request) {

	 		ErrorResponse errorResponse =new ErrorResponse();
	 		errorResponse.setTimestamp(LocalDateTime.now());
	 		errorResponse.setErrorCode(ex.getErrorCode());
	 		errorResponse.setErrorMsg(ex.getMessage());

	 		errorResponse.setCallerUri(request.getRequestURI());

			return errorResponse;
		}
	 
	 	/*@Override
	    protected ResponseEntity<Object> handleMethodArgumentNotValid(
	        MethodArgumentNotValidException ex, HttpHeaders headers, 
	        HttpStatus status, WebRequest request) {

	        Map<String, Object> body = new LinkedHashMap<>();
	        body.put("timestamp", LocalDate.now());
	        body.put("status", status.value());

	        List<String> errors = ex.getBindingResult()
	                .getFieldErrors()
	                .stream()
	                .map(x -> x.getDefaultMessage())
	                .collect(Collectors.toList());

	        body.put("errors", errors);

	        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	    }*/
	 
}
