package com.besmartexim.dto.request;

import javax.validation.constraints.NotBlank;

public class UserRequest {
	
	private String firstname;
	private String lastname;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String mobile;
	
	private String companyName;
	
	private String password;
	
	@NotBlank
	private String userType;
	
	private Long subscriptionId;
	
	private Long uplineId;
	
	private Long downloadLimit;
	
	private String isActive;
	
	private String isDelete;
	
	private Long memberId;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Long getUplineId() {
		return uplineId;
	}

	public void setUplineId(Long uplineId) {
		this.uplineId = uplineId;
	}

	public Long getDownloadLimit() {
		return downloadLimit;
	}

	public void setDownloadLimit(Long downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	
	

}
