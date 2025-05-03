package com.besmartexim.dto.response;

import java.util.List;

public class UserListResponse {
	
	List<UserDetailsResponse> userList;

	public List<UserDetailsResponse> getUserList() {
		return userList;
	}

	public void setUserList(List<UserDetailsResponse> userList) {
		this.userList = userList;
	}
	
	

}
