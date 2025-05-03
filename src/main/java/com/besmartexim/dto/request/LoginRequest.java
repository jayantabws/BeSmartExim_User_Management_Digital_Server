package com.besmartexim.dto.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
	private String ipaddress;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	

	
}
