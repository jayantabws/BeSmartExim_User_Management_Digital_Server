package com.besmartexim.dto.request;

public class AdminPermissionRequest {

	private Long id;
	private Long userId;
	private String users;
	private String adminUsers;
	private String subscriptions;
	private String activityLog;
	private String countries;
	private String contacts;
	private String siteSettings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getAdminUsers() {
		return adminUsers;
	}

	public void setAdminUsers(String adminUsers) {
		this.adminUsers = adminUsers;
	}

	public String getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(String subscriptions) {
		this.subscriptions = subscriptions;
	}

	public String getActivityLog() {
		return activityLog;
	}

	public void setActivityLog(String activityLog) {
		this.activityLog = activityLog;
	}

	public String getCountries() {
		return countries;
	}

	public void setCountries(String countries) {
		this.countries = countries;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getSiteSettings() {
		return siteSettings;
	}

	public void setSiteSettings(String siteSettings) {
		this.siteSettings = siteSettings;
	}

}
