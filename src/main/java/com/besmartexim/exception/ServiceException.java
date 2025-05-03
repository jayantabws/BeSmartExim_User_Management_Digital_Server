package com.besmartexim.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -470180507998010368L;
	private String errorCode; 
	
	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String errorCode,String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}