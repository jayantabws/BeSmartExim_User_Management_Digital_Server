package com.besmartexim.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

@CrossOrigin
@RestController
@RequestMapping(path = "/user-management")
public class UserManagementController {

	private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

	@Autowired
	private UserManagementService userManagementService;

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userLogin(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
		logger.info("Request : /user-management/login");
		LoginResponse loginResponse = userManagementService.userLogin(loginRequest);
		logger.info("Response : " + loginResponse);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/forgotpassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> forgotPassword(@RequestParam(required = true) String userEmail) throws Exception {
		logger.info("Request : /user-management/forgotpassword");
		ForgotPasswordResponse response = userManagementService.forgotPassword(userEmail);
		logger.info("Response : " + response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/adminlogin", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> adminLogin(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
		logger.info("Request : /user-management/adminlogin");
		LoginResponse loginResponse = userManagementService.adminLogin(loginRequest);
		logger.info("Response : " + loginResponse);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userCreate(@RequestBody UserRequest userRequest,
			@RequestHeader(required = false) Long accessedBy) throws Exception {
		logger.info("Request : /user-management/user");
		userManagementService.userCreate(userRequest, accessedBy);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping(value = "/user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userUpdate(@RequestBody UserRequest userRequest, @PathVariable Long userId,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		userManagementService.userUpdate(userRequest, userId, accessedBy);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PutMapping(value = "/deleteuser/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userDelete(@RequestBody UserRequest userRequest, @PathVariable Long userId,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		userManagementService.userDelete(userRequest, userId, accessedBy);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping(value = "/user/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDetailsResponse> userDetails(@RequestParam Long userId,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		UserDetailsResponse userDetailsResponse = userManagementService.userDetails(userId);

		return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);

	}

	@GetMapping(value = "/user/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserListResponse> userList(@RequestParam(required = false) Long uplineId,
			@RequestParam(required = false) String userType, @RequestParam(required = false) String isExpired,
			@RequestParam(required = false) String isActive, @RequestHeader(required = true) Long accessedBy)
			throws Exception {
		logger.info("accessedBy = " + accessedBy);

		UserListResponse userListResponse = userManagementService.userList(uplineId, userType, accessedBy, isExpired,
				isActive);

		return new ResponseEntity<>(userListResponse, HttpStatus.OK);

	}

	@PostMapping(value = "/user-subscription/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUserSubscription(@RequestBody @Valid UserSubscriptionRequest userSubscriptionRequest,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		userManagementService.createUserSubscription(userSubscriptionRequest, accessedBy);

		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@PutMapping(value = "/user-subscription/update/{userSubscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateUserSubscription(
			@RequestBody UserSubscriptionDetailsRequest userSubscriptionDetailsRequest,
			@PathVariable Long userSubscriptionId, @RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		String msg = userManagementService.updateUserSubscription(userSubscriptionDetailsRequest, userSubscriptionId, accessedBy);

		return ResponseEntity.ok(msg);

	}

	@GetMapping(value = "/user-subscription/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserSubscriptionList> listUserSubscription(@RequestParam(required = true) Long userId,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		UserSubscriptionList userSubscriptionList = userManagementService.userSubscriptionList(userId, accessedBy);

		return new ResponseEntity<>(userSubscriptionList, HttpStatus.OK);

	}

	@GetMapping(value = "/user-subscription/activelist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserSubscriptionList> activeListUserSubscription(@RequestParam(required = true) Long userId,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		UserSubscriptionList userSubscriptionList = userManagementService.activeUserSubscriptionList(userId,
				accessedBy);

		return new ResponseEntity<>(userSubscriptionList, HttpStatus.OK);

	}

	@PutMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userLogout(@RequestBody @Valid LogoutRequest logoutRequest,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		userManagementService.userLogout(logoutRequest, accessedBy);

		return new ResponseEntity<>(HttpStatus.OK);

	}


	@GetMapping(value = "/user/loginlist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginListResponse> loginList(@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long uplineId, @RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		Date fd = null, td = null;

		if (fromDate != null && fromDate != "") {
			fd = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		}
		if (toDate != null && toDate != "") {
			toDate = toDate + " 23:59:59.999";
			td = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(toDate);
		}
		if (td == null && fd != null) {
			td = new Date();
		}

		LoginListResponse loginListRespose = userManagementService.loginList(userId, uplineId, pageNumber, accessedBy,
				fd, td);

		return new ResponseEntity<>(loginListRespose, HttpStatus.OK);

	}

	@GetMapping(value = "/user/loginlistcount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> loginListCount(@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long uplineId, @RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate, @RequestHeader(required = true) Long accessedBy)
			throws Exception {
		logger.info("accessedBy = " + accessedBy);

		Date fd = null, td = null;

		if (fromDate != null && fromDate != "") {
			fd = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		}
		if (toDate != null && toDate != "") {
			toDate = toDate + " 23:59:59.999";
			td = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(toDate);
		}
		if (td == null && fd != null) {
			td = new Date();
		}

		Long count = userManagementService.loginListCount(userId, uplineId, accessedBy, fd, td);

		return new ResponseEntity<>(count, HttpStatus.OK);

	}

	@GetMapping(value = "/user/loginstatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginList> loginStatus(@RequestParam(required = true) Long loginId,
			@RequestHeader(required = true) Long accessedBy) throws Exception {
		logger.info("accessedBy = " + accessedBy);

		LoginList loginList = userManagementService.loginStatus(loginId, accessedBy);

		return new ResponseEntity<>(loginList, HttpStatus.OK);

	}

	@GetMapping(value = "/loginusers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LoginList>> currentloginusers(@RequestHeader(required = true) Long accessedBy)
			throws Exception {
		logger.info("Request : /user-management/loginusers");

		List<LoginList> loginList = userManagementService.currentLoginUsers(accessedBy);

		return new ResponseEntity<>(loginList, HttpStatus.OK);
	}

	@GetMapping(value = "/loginusercount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> currentloginuserCounts(@RequestHeader(required = true) Long accessedBy)
			throws Exception {
		logger.info("Request : /user-management/loginusercount");

		Long count = userManagementService.currentLoginUserCount(accessedBy);

		return new ResponseEntity<>(count, HttpStatus.OK);
	}

}
