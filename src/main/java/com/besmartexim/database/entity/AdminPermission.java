package com.besmartexim.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin_permission")
public class AdminPermission {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "users")
	private String users;
	
	@Column(name = "admin_users")
	private String adminUsers;
	
	@Column(name = "subscriptions")
	private String subscriptions;
	
	@Column(name = "activity_log")
	private String activityLog;
	
	@Column(name = "countries")
	private String countries;
	
	@Column(name = "contacts")
	private String contacts;
	
	@Column(name = "site_settings")
	private String siteSettings;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "modified_by")
	private Long modifiedBy;
	
	@Column(name = "modified_date")
	private Date modifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public AdminPermission() {
		super();
	}

	public AdminPermission(Long userId, String users, String adminUsers, String subscriptions, String activityLog,
			String countries, String contacts, String siteSettings, Long createdBy, Date createdDate) {
		super();
		this.userId = userId;
		this.users = users;
		this.adminUsers = adminUsers;
		this.subscriptions = subscriptions;
		this.activityLog = activityLog;
		this.countries = countries;
		this.contacts = contacts;
		this.siteSettings = siteSettings;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}
}
