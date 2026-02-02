package com.besmartexim.database.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_subscription")
public class UserSubscription {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "subscription_id")
	private Long subscriptionId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "price")
	private Long price;
	
	@Column(name = "validity_in_day")
	private Integer validityInDay;
	
	@Column(name = "countries")
	private String countries;
	
	@Column(name = "continent")
	private String continent;
	
	@Column(name = "download_limit")
	private String downloadLimit;
	
	@Column(name = "total_workspace")
	private String totalWorkspace;
	
	@Column(name = "sub_user_count")
	private String subUserCount;
	
	@Column(name = "data_access_in_month")
	private String dataAccessInMonth;
	
	@Column(name = "download_per_day")
	private String downloadPerDay;
	
	@Column(name = "support")
	private String support;
	
	@Column(name = "ticket_manager")
	private String ticketManager;
	
	@Column(name = "records_per_workspace")
	private String recordsPerWorkspace;
	
	@Column(name = "display_fields")
	private String displayFields;
	
	@Column(name = "query_per_day")
	private String queryPerDay;
	
	@Column(name = "account_expire_date")
	private Date accountExpireDate;	
	
	@Column(name = "is_active")
	private String isActive;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "modified_by")
	private Long modifiedBy;
	
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@Column(name = "allowed_chapter")
	private String allowedChapter;
	
	@Column(name = "data_access_upto")
	private String dataAccessUpto;
	
	@Column(name = "user_indepth_access")
	private String indepthAccess;

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

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Integer getValidityInDay() {
		return validityInDay;
	}

	public void setValidityInDay(Integer validityInDay) {
		this.validityInDay = validityInDay;
	}

	public String getCountries() {
		return countries;
	}

	public void setCountries(String countries) {
		this.countries = countries;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getDownloadLimit() {
		return downloadLimit;
	}

	public void setDownloadLimit(String downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

	public String getTotalWorkspace() {
		return totalWorkspace;
	}

	public void setTotalWorkspace(String totalWorkspace) {
		this.totalWorkspace = totalWorkspace;
	}

	public String getSubUserCount() {
		return subUserCount;
	}

	public void setSubUserCount(String subUserCount) {
		this.subUserCount = subUserCount;
	}

	public String getDataAccessInMonth() {
		return dataAccessInMonth;
	}

	public void setDataAccessInMonth(String dataAccessInMonth) {
		this.dataAccessInMonth = dataAccessInMonth;
	}

	public String getDownloadPerDay() {
		return downloadPerDay;
	}

	public void setDownloadPerDay(String downloadPerDay) {
		this.downloadPerDay = downloadPerDay;
	}

	public String getSupport() {
		return support;
	}

	public void setSupport(String support) {
		this.support = support;
	}

	public String getTicketManager() {
		return ticketManager;
	}

	public void setTicketManager(String ticketManager) {
		this.ticketManager = ticketManager;
	}

	public String getRecordsPerWorkspace() {
		return recordsPerWorkspace;
	}

	public void setRecordsPerWorkspace(String recordsPerWorkspace) {
		this.recordsPerWorkspace = recordsPerWorkspace;
	}

	public String getDisplayFields() {
		return displayFields;
	}

	public void setDisplayFields(String displayFields) {
		this.displayFields = displayFields;
	}

	public String getQueryPerDay() {
		return queryPerDay;
	}

	public void setQueryPerDay(String queryPerDay) {
		this.queryPerDay = queryPerDay;
	}

	public Date getAccountExpireDate() {
		return accountExpireDate;
	}

	public void setAccountExpireDate(Date accountExpireDate) {
		this.accountExpireDate = accountExpireDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	public String getAllowedChapter() {
		return allowedChapter;
	}

	public void setAllowedChapter(String allowedChapter) {
		this.allowedChapter = allowedChapter;
	}

	public String getDataAccessUpto() {
		return dataAccessUpto;
	}

	public void setDataAccessUpto(String dataAccessUpto) {
		this.dataAccessUpto = dataAccessUpto;
	}

	public String getIndepthAccess() {
		return indepthAccess;
	}

	public void setIndepthAccess(String indepthAccess) {
		this.indepthAccess = indepthAccess;
	}
	
	

	
	
}
