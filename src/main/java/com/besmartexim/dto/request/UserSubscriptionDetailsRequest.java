package com.besmartexim.dto.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSubscriptionDetailsRequest {
	
	private Long id;
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
	private String allowedChapter;
	private String dataAccessUpto;
	private String indepthAccess;
	private ArrayList<String> countryId;
	
	
		
	public ArrayList<String> getCountryId() {
		return countryId;
	}
	public void setCountryId(ArrayList<String> countryId) {
		this.countryId = countryId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
