package com.besmartexim.dto.response;

import java.util.List;

public class Subscription {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private Long price;
	
	private Integer validityInDay;
	
	private String isCustom;
	
	private String isActive;
	
	private List<String> countryId;
	
	private List<String> continentId;
	
	private String dataAccess;
	
	private String downloadLimit;
	
	private String maxDownloadPerDay;
	
	private String workspaceLimit;
	
	private String support;
	
	private String ticketManager;	
	
	private String recordPerWorkspace;
	
	private String subUserCount;
	
	private String displayFields;
	
	private String searchQueryPerDay;
	
	private String allowedChapter;
	
	private String indepthAccess;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(String isCustom) {
		this.isCustom = isCustom;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public List<String> getCountryId() {
		return countryId;
	}

	public void setCountryId(List<String> countryId) {
		this.countryId = countryId;
	}

	public List<String> getContinentId() {
		return continentId;
	}

	public void setContinentId(List<String> continentId) {
		this.continentId = continentId;
	}

	public String getDataAccess() {
		return dataAccess;
	}

	public void setDataAccess(String dataAccess) {
		this.dataAccess = dataAccess;
	}

	public String getDownloadLimit() {
		return downloadLimit;
	}

	public void setDownloadLimit(String downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

	public String getMaxDownloadPerDay() {
		return maxDownloadPerDay;
	}

	public void setMaxDownloadPerDay(String maxDownloadPerDay) {
		this.maxDownloadPerDay = maxDownloadPerDay;
	}

	public String getWorkspaceLimit() {
		return workspaceLimit;
	}

	public void setWorkspaceLimit(String workspaceLimit) {
		this.workspaceLimit = workspaceLimit;
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

	public String getRecordPerWorkspace() {
		return recordPerWorkspace;
	}

	public void setRecordPerWorkspace(String recordPerWorkspace) {
		this.recordPerWorkspace = recordPerWorkspace;
	}

	public String getSubUserCount() {
		return subUserCount;
	}

	public void setSubUserCount(String subUserCount) {
		this.subUserCount = subUserCount;
	}

	public String getDisplayFields() {
		return displayFields;
	}

	public void setDisplayFields(String displayFields) {
		this.displayFields = displayFields;
	}

	public String getSearchQueryPerDay() {
		return searchQueryPerDay;
	}

	public void setSearchQueryPerDay(String searchQueryPerDay) {
		this.searchQueryPerDay = searchQueryPerDay;
	}

	public String getAllowedChapter() {
		return allowedChapter;
	}

	public void setAllowedChapter(String allowedChapter) {
		this.allowedChapter = allowedChapter;
	}

	public String getIndepthAccess() {
		return indepthAccess;
	}

	public void setIndepthAccess(String indepthAccess) {
		this.indepthAccess = indepthAccess;
	}

	

}
