package com.besmartexim.dto.response;

import java.time.LocalDateTime;

public class ErrorResponse {

	private LocalDateTime timestamp;
	private String errorCode;
	private String errorMsg;
	private String errorDesc;
	private String callerUri;
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getCallerUri() {
		return callerUri;
	}
	public void setCallerUri(String callerUri) {
		this.callerUri = callerUri;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
	
}
