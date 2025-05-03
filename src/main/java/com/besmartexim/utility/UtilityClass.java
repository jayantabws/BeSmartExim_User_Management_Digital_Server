package com.besmartexim.utility;

public class UtilityClass {

	public static boolean isEmpty(String inputStr) {
		if(null==inputStr || inputStr.trim().equals("")){
			return true;
		}else {
			return false;
		}
	}
}
