package com.besmartexim.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.besmartexim.dto.request.LoginDetailsRequest;
import com.besmartexim.dto.request.LoginRequest;
import com.besmartexim.dto.request.LogoutRequest;
import com.besmartexim.dto.request.UserRequest;
import com.besmartexim.dto.request.UserSubscriptionDetailsRequest;
import com.besmartexim.dto.request.UserSubscriptionRequest;
import com.besmartexim.dto.response.ForgotPasswordResponse;
import com.besmartexim.dto.response.LoginList;
import com.besmartexim.dto.response.LoginListResponse;
import com.besmartexim.dto.response.LoginResponse;
import com.besmartexim.dto.response.UserDetailsResponse;
import com.besmartexim.dto.response.UserListResponse;
import com.besmartexim.dto.response.UserSubscriptionList;
import com.besmartexim.service.UserManagementService;
//@CrossOrigin
@RestController
@RequestMapping(path="/user-management")
public class UserManagementController {

	
private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);
	
	@Autowired
	private UserManagementService userManagementService;

	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userLogin(@RequestBody @Valid LoginRequest loginRequest) throws Exception{
		logger.info("Request : /user-management/login");
		LoginResponse loginResponse = userManagementService.userLogin(loginRequest);
		logger.info("Response : " + loginResponse);
		return new ResponseEntity<>(loginResponse,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> forgotPassword(@RequestParam (required=true) String userEmail) throws Exception{
		logger.info("Request : /user-management/forgotpassword");
		ForgotPasswordResponse response = userManagementService.forgotPassword(userEmail);
		logger.info("Response : " + response);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/adminlogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> adminLogin(@RequestBody @Valid LoginRequest loginRequest) throws Exception{
		logger.info("Request : /user-management/adminlogin");
		LoginResponse loginResponse = userManagementService.adminLogin(loginRequest);
		logger.info("Response : " + loginResponse);
		return new ResponseEntity<>(loginResponse,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userCreate(@RequestBody  UserRequest userRequest, @RequestHeader(value="accessedBy", required=false) Long accessedBy ) throws Exception{
		logger.info("Request : /user-management/user");
		userManagementService.userCreate(userRequest, accessedBy);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity userUpdate(@RequestBody  UserRequest userRequest,@PathVariable Long userId,@RequestHeader(value="accessedBy", required=true) Long accessedBy ) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		userManagementService.userUpdate(userRequest,userId,accessedBy);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/deleteuser/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity userDelete(@RequestBody  UserRequest userRequest,@PathVariable Long userId,@RequestHeader(value="accessedBy", required=true) Long accessedBy ) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		userManagementService.userDelete(userRequest,userId,accessedBy);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity userDetails(@RequestParam Long userId, @RequestHeader(value="accessedBy", required=true) Long accessedBy) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		UserDetailsResponse userDetailsResponse =  userManagementService.userDetails(userId);
		
		return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity userList(@RequestParam(value = "uplineId", required = false) Long uplineId, @RequestParam(value = "userType", required = false) String userType, @RequestParam(value = "isExpired", required = false) String isExpired,@RequestParam(value = "isActive", required = false) String isActive, @RequestHeader(value="accessedBy", required=true) Long accessedBy) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		UserListResponse userListResponse =  userManagementService.userList(uplineId, userType, accessedBy, isExpired, isActive);
		
		return new ResponseEntity<>(userListResponse, HttpStatus.OK);
		
	}
	
		
	@RequestMapping(value = "/user-subscription/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createUserSubscription(@RequestBody @Valid UserSubscriptionRequest userSubscriptionRequest,@RequestHeader(value="accessedBy", required=true) Long accessedBy) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		userManagementService.createUserSubscription(userSubscriptionRequest, accessedBy);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/user-subscription/update/{userSubscriptionId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateUserSubscription(@RequestBody UserSubscriptionDetailsRequest userSubscriptionDetailsRequest,@PathVariable Long userSubscriptionId,@RequestHeader(value="accessedBy", required=true) Long accessedBy) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		userManagementService.updateUserSubscription(userSubscriptionDetailsRequest,userSubscriptionId, accessedBy);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user-subscription/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listUserSubscription(@RequestParam(value = "userId", required = true) Long userId, @RequestHeader(value="accessedBy", required=true) Long accessedBy) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		UserSubscriptionList userSubscriptionList  =  userManagementService.userSubscriptionList(userId,accessedBy);
		
		return new ResponseEntity<>(userSubscriptionList, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user-subscription/activelist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity activeListUserSubscription(@RequestParam(value = "userId", required = true) Long userId, @RequestHeader(value="accessedBy", required=true) Long accessedBy) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		UserSubscriptionList userSubscriptionList  =  userManagementService.activeUserSubscriptionList(userId,accessedBy);
		
		return new ResponseEntity<>(userSubscriptionList, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity userLogout(@RequestBody  @Valid LogoutRequest logoutRequest,@RequestHeader(value="accessedBy", required=true) Long accessedBy ) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		userManagementService.userLogout(logoutRequest,accessedBy);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
//	@RequestMapping(value = "/user/loginlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity loginList(@RequestParam(required=false) Long userId , @RequestParam (required=false) Long uplineId, @RequestHeader(value="accessedBy", required=true) Long accessedBy ) throws Exception{
//		logger.info("accessedBy = "+accessedBy);
//			
//		LoginListResponse loginListRespose =userManagementService.loginList(userId,uplineId,accessedBy);
//		
//		return new ResponseEntity<>(loginListRespose, HttpStatus.OK);
//		
//	}
	
	
	@RequestMapping(value = "/user/loginlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity loginList(@RequestParam(required=false) Long userId , @RequestParam (required=false) Long uplineId, @RequestParam (defaultValue = "1") int pageNumber, @RequestHeader(value="accessedBy", required=true) Long accessedBy ) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		LoginListResponse loginListRespose =userManagementService.loginList(userId,uplineId,pageNumber,accessedBy);
		
		return new ResponseEntity<>(loginListRespose, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user/loginlistcount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> loginListCount(@RequestParam(required=false) Long userId , @RequestParam (required=false) Long uplineId, @RequestHeader(value="accessedBy", required=true) Long accessedBy ) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		Long count =userManagementService.loginListCount(userId,uplineId,accessedBy);
		
		return new ResponseEntity<>(count, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/user/loginstatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity loginStatus(@RequestParam(required=true) Long loginId , @RequestHeader(value="accessedBy", required=true) Long accessedBy ) throws Exception{
		logger.info("accessedBy = "+accessedBy);
			
		LoginList loginList =userManagementService.loginStatus(loginId,accessedBy);
		
		return new ResponseEntity<>(loginList, HttpStatus.OK);
		
	}
	
	
	
}
