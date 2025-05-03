package com.besmartexim.utility;

import java.util.HashMap;
import java.util.Map;

public class AppConstant {
	public static final String USER_ERROR_CODE1="USER_ERROR_001";
	public static final String USER_ERROR_CODE2="USER_ERROR_002";
	public static final String USER_ERROR_CODE3="USER_ERROR_003";
	public static final String USER_ERROR_CODE4="USER_ERROR_004";
	public static final String USER_ERROR_CODE5="USER_ERROR_005";
	
	public static Map<String, String> errormap;
	static {
		errormap = new HashMap<String, String>();
		errormap.put("USER_ERROR_001","Incorrect login credential");
		errormap.put("USER_ERROR_002","Inactive user account");
		errormap.put("USER_ERROR_003","Removed user account");
		errormap.put("USER_ERROR_004","Not active subscription is found");
		errormap.put("USER_ERROR_005","Invalid user name");
	}
	
	
}
