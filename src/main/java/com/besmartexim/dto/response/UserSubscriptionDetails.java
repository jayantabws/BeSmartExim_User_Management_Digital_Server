package com.besmartexim.dto.response;

import java.util.Date;
import java.util.List;

public class UserSubscriptionDetails {

	private Long id;

	private Long userId;

	private Long subscriptionId;

	private String name;

	private String description;

	private Long price;

	private Integer validityInDay;

	private List<String> countries;

	private List<String> continent;

	private String downloadLimit;

	private String totalWorkspace;

	private String subUserCount;

	private String dataAccessInMonth;

	private String downloadPerDay;

	private String support;

	private String ticketManager;

	private String recordsPerWorkspace;

	private String displayFields;

	private String queryPerDay;

	private Date accountExpireDate;

	private String isActive;

	private Long createdBy;

	private Date createdDate;

	private Long modifiedBy;

	private Date modifiedDate;

	private String allowedChapter;

	private String dataAccessUpto;

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

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public List<String> getContinent() {
		return continent;
	}

	public void setContinent(List<String> continent) {
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
