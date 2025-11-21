package com.besmartexim.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.besmartexim.database.entity.LoginDetails;
import com.besmartexim.database.entity.User;
import com.besmartexim.database.entity.UserSubscription;
import com.besmartexim.database.repository.LoginDetailsRepository;
import com.besmartexim.database.repository.UserRepository;
import com.besmartexim.database.repository.UserSubscriptionRepository;
import com.besmartexim.dto.request.LoginRequest;
import com.besmartexim.dto.request.LogoutRequest;
import com.besmartexim.dto.request.UserRequest;
import com.besmartexim.dto.request.UserSubscriptionDetailsRequest;
import com.besmartexim.dto.request.UserSubscriptionRequest;
import com.besmartexim.dto.response.ForgotPasswordResponse;
import com.besmartexim.dto.response.LoginList;
import com.besmartexim.dto.response.LoginListResponse;
import com.besmartexim.dto.response.LoginResponse;
import com.besmartexim.dto.response.Subscription;
import com.besmartexim.dto.response.UserDetailsResponse;
import com.besmartexim.dto.response.UserListResponse;
import com.besmartexim.dto.response.UserSubscriptionDetails;
import com.besmartexim.dto.response.UserSubscriptionList;
import com.besmartexim.exception.ServiceException;
import com.besmartexim.utility.AppConstant;
import com.besmartexim.utility.QueryConstant;

@Service
public class UserManagementService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;

	@Autowired
	private LoginDetailsRepository loginDetailsRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

	@Value("${subscription.service.url}")
	private String subscriptionUrl;

	@Value("${smtp.service.url}")
	private String SMTP_HOST_NAME;

	@Value("${smtp.service.port}")
	private int SMTP_HOST_PORT;

	@Value("${smtp.service.user}")
	private String SMTP_AUTH_USER;

	@Value("${smtp.service.pwd}")
	private String SMTP_AUTH_PWD;

	@Autowired
	private RestTemplate restTemplate;

	public LoginResponse userLogin(LoginRequest loginRequest) throws Exception {

		LoginResponse loginResponse = new LoginResponse();

		// User userEntity =
		// userRepository.findByEmailAndPasswordAndUserType(loginRequest.getEmail(),
		// loginRequest.getPassword(),"USER");
		User userEntity = userRepository.findByEmailAndPasswordAndUserTypeAndIsDelete(loginRequest.getEmail(),
				loginRequest.getPassword(), "USER", "N");
		if (userEntity == null) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE1,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE1));
		} else if (userEntity.getIsActive().equals("N")) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE2,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE2));
		} else if (userEntity.getIsDelete().equals("Y")) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE3,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE3));
		} else {

			List<UserSubscription> subsList = null;

			Long uplineId = userEntity.getUplineId();
			if (uplineId == 0)
				subsList = userSubscriptionRepository.findAllByUserIdAndIsActiveOrderByIdDesc(userEntity.getId(), "Y");
			else
				subsList = userSubscriptionRepository.findAllByUserIdAndIsActiveOrderByIdDesc(uplineId, "Y");

			if (subsList.isEmpty() || subsList.get(0).getAccountExpireDate().getTime() < new Date().getTime()) {
				throw new ServiceException(AppConstant.USER_ERROR_CODE4,
						AppConstant.errormap.get(AppConstant.USER_ERROR_CODE4));
			}

			UserSubscription userSubscription = subsList.get(0);

			Date lastLoginTime = loginDetailsRepository.findByUplineIdMaxLoginDate(userSubscription.getUserId());

			Date currentDate = new Date();

			if (lastLoginTime != null && ((lastLoginTime.getDate() != currentDate.getDate())
					|| lastLoginTime.getMonth() != currentDate.getMonth()
					|| lastLoginTime.getYear() != currentDate.getYear())) {

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.add("accessedBy", "" + userSubscription.getUserId());
				headers.add("Authorization", "Basic YXBpLWV4aW13YXRjaDp1ZTg0Q1JSZnRAWGhBMyRG");

				ResponseEntity<Subscription> responseEntity = restTemplate.exchange(
						subscriptionUrl + userSubscription.getSubscriptionId(), HttpMethod.GET,
						new HttpEntity<Object>(headers), Subscription.class);
				Subscription subscription = responseEntity.getBody();

				userSubscription.setQueryPerDay(subscription.getSearchQueryPerDay());
				userSubscriptionRepository.save(userSubscription);
			}

			List<LoginDetails> list = loginDetailsRepository.findByLogoutTimeNULL(userEntity.getId());
			for (LoginDetails loginDetails : list) {
				loginDetails.setLogoutTime(new Date());
				loginDetailsRepository.save(loginDetails);
			}

			LoginDetails loginDetailsEntity = new LoginDetails();
			loginDetailsEntity.setUserId(userEntity.getId());
			loginDetailsEntity.setIpAddress(loginRequest.getIpaddress());
			loginDetailsEntity.setSessionId(UUID.randomUUID().toString());
			loginDetailsEntity.setLoginTime(new Date());
			loginDetailsRepository.save(loginDetailsEntity);

			loginResponse.setMessage("Login Successful");
			loginResponse.setStatus("1");
			loginResponse.setUserid(userEntity.getId());
			loginResponse.setFirstname(userEntity.getFirstname());
			loginResponse.setLastname(userEntity.getLastname());
			loginResponse.setEmail(userEntity.getEmail());
			loginResponse.setMobile(userEntity.getMobile());
			loginResponse.setCompanyName(userEntity.getCompanyName());
			loginResponse.setUplineId(userEntity.getUplineId());
			loginResponse.setDownloadLimit(userEntity.getDownloadLimit());
			loginResponse.setIsActive(userEntity.getIsActive());
			loginResponse.setCreatedDate(userEntity.getCreatedDate());
			loginResponse.setPassword(userEntity.getPassword());
			loginResponse.setLoginId(loginDetailsEntity.getId());
			loginResponse.setSessionId(loginDetailsEntity.getSessionId());
		}
		// sendEmail(loginRequest.getEmail());
		return loginResponse;
	}

	public ForgotPasswordResponse forgotPassword(String userEmail) throws Exception {
		// TODO Auto-generated method stub

		ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();
		String randomNumber = "";

		User userEntity = userRepository.findByEmailAndUserType(userEmail, "USER");
		if (userEntity == null) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE5,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE5));
		} else if (userEntity.getIsActive().equals("N")) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE2,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE2));
		} else if (userEntity.getIsDelete().equals("Y")) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE3,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE3));
		} else {
			Random rand = new Random();
			randomNumber = String.format("%04d", rand.nextInt(10000));

			forgotPasswordResponse.setUserid(userEntity.getId());
			forgotPasswordResponse.setFirstname(userEntity.getFirstname());
			forgotPasswordResponse.setLastname(userEntity.getLastname());
			forgotPasswordResponse.setEmail(userEmail);
			forgotPasswordResponse.setOtp(randomNumber);
			forgotPasswordResponse.setMessage("SENT");
			forgotPasswordEmail(userEmail, userEntity.getFirstname(), randomNumber);
		}

		return forgotPasswordResponse;
	}

	@SuppressWarnings("unused")
	private boolean sendEmail(String emailId) {
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");

			Session mailSession = Session.getDefaultInstance(props);
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);

			message.setSubject("Be Datos - Login");
			message.setContent("Thanks for login into BE Datos - Exim Trade Portal.", "text/html");
			Address[] from = InternetAddress.parse("accounts@eximwatch.com");// Your domain email
			message.addFrom(from);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailId)); // Send email To (Type email
																							// ID that you want to send)

			transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean forgotPasswordEmail(String emailId, String firstName, String randomNumber) {
		try {
			String msgBody = "";
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");

			Session mailSession = Session.getDefaultInstance(props);
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);

			message.setSubject("Password Reset Request for BE Datos Account");

			msgBody = "Dear " + firstName
					+ ",<br><br>We have received a request to reset your password for the BE Datos Online Platform. If you did not initiate this request, please kindly disregard this email.<br><br>";
			msgBody = msgBody + "Here is your 4 digit auto generated pin: " + randomNumber + ".<br><br>";
			msgBody = msgBody
					+ "Please be advised that this pin will expire after a certain period for security reasons.";
			msgBody = msgBody
					+ "Should you have any questions or concerns, please do not hesitate to contact our support team at (+91) 9073371811/9051062400.<br><br>";
			msgBody = msgBody + "Thank you for your attention to this matter.<br><br>";
			msgBody = msgBody + "Thank you for choosing BE Datos.<br><br><br>";
			msgBody = msgBody + "Best regards,<br><br>";
			msgBody = msgBody + "BE Datos";

			message.setContent(msgBody, "text/html");
			Address[] from = InternetAddress.parse("accounts@eximwatch.com");// Your domain email
			message.addFrom(from);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailId)); // Send email To (Type email
																							// ID that you want to send)

			transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public LoginResponse adminLogin(LoginRequest loginRequest) throws Exception {

		LoginResponse loginResponse = new LoginResponse();

		User userEntity = userRepository.findByEmailAndPasswordAndUserType(loginRequest.getEmail(),
				loginRequest.getPassword(), "ADMIN");
		if (userEntity == null) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE1,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE1));
		} else if (userEntity.getIsActive().equals("N")) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE2,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE2));
		} else if (userEntity.getIsDelete().equals("Y")) {
			throw new ServiceException(AppConstant.USER_ERROR_CODE3,
					AppConstant.errormap.get(AppConstant.USER_ERROR_CODE3));
		} else {
			loginResponse.setMessage("Login Successful");
			loginResponse.setStatus("1");
			loginResponse.setUserid(userEntity.getId());
			loginResponse.setFirstname(userEntity.getFirstname());
			loginResponse.setLastname(userEntity.getLastname());
			loginResponse.setEmail(userEntity.getEmail());
			loginResponse.setMobile(userEntity.getMobile());

			LoginDetails loginDetailsEntity = new LoginDetails();
			loginDetailsEntity.setUserId(userEntity.getId());
			loginDetailsEntity.setIpAddress(loginRequest.getIpaddress());
			loginDetailsEntity.setSessionId(UUID.randomUUID().toString());
			loginDetailsEntity.setLoginTime(new Date());
			loginDetailsRepository.save(loginDetailsEntity);

			loginResponse.setLoginId(loginDetailsEntity.getId());
			loginResponse.setSessionId(loginDetailsEntity.getSessionId());
		}
		return loginResponse;
	}

	public void userCreate(UserRequest request, Long accessedBy) throws Exception {

		String firstname = request.getFirstname();
		String lastname = request.getLastname();
		String email = request.getEmail();
		String mobile = request.getMobile();
		String companyName = request.getCompanyName();
		String userType = request.getUserType();
		String password = request.getPassword();
		String isActive = request.getIsActive();
		Long subscriptionId = request.getSubscriptionId();
		Long uplineId = request.getUplineId();
		Long downloadLimit = request.getDownloadLimit();
		Long memberId = request.getMemberId();
		User user = userRepository.findByEmailAndIsDelete(email, "N");
		if (user != null) {
			throw new ServiceException("User already exist");
		} else {
			User userEntity = new User();
			if (null != firstname)
				userEntity.setFirstname(firstname);
			if (null != lastname)
				userEntity.setLastname(lastname);
			if (null != email)
				userEntity.setEmail(email);
			if (null != mobile)
				userEntity.setMobile(mobile);
			if (null != companyName)
				userEntity.setCompanyName(companyName);
			if (null != userType)
				userEntity.setUserType(userType);
			if (null != isActive)
				userEntity.setIsActive(request.getIsActive());
			else
				userEntity.setIsActive("N");
			userEntity.setIsDelete("N");
			if (null != password)
				userEntity.setPassword(request.getPassword());
			else
				userEntity.setPassword("Be12345#");
			userEntity.setCreatedDate(new Date());
			if (null != uplineId)
				userEntity.setUplineId(uplineId);
			else
				userEntity.setUplineId((long) 0);
			if (null != downloadLimit)
				userEntity.setDownloadLimit(downloadLimit);
			else
				userEntity.setDownloadLimit((long) 0);
			if (null != accessedBy)
				userEntity.setCreatedBy(accessedBy);
			if (null != memberId)
				userEntity.setMemberId(memberId);
			userEntity = userRepository.save(userEntity);

			if (null != subscriptionId) {
				UserSubscription userSubscriptionEntity = new UserSubscription();
				userSubscriptionEntity.setUserId(userEntity.getId());
				userSubscriptionEntity.setSubscriptionId(subscriptionId);
				userSubscriptionEntity.setCreatedBy(userEntity.getId());
				userSubscriptionEntity.setCreatedDate(new Date());
				userSubscriptionRepository.save(userSubscriptionEntity);
			}

			if (null != uplineId) {
				subUserWelcomeEmail(userEntity.getId(), uplineId);
			}

		}

	}

	public void userUpdate(UserRequest request, Long userId, Long accessedBy) throws Exception {
		User userEntity = userRepository.findById(userId).get();
		if (userEntity == null) {
			throw new ServiceException("User is not registered");
		} else {
			// User userEntity = list.get();
			if (null != request.getFirstname())
				userEntity.setFirstname(request.getFirstname());
			if (null != request.getLastname())
				userEntity.setLastname(request.getLastname());
			if (null != request.getCompanyName())
				userEntity.setCompanyName(request.getCompanyName());
			if (null != request.getUserType())
				userEntity.setUserType(request.getUserType());
			if (null != request.getPassword())
				userEntity.setPassword(request.getPassword());
			if (null != request.getMobile())
				userEntity.setMobile(request.getMobile());
			if (null != request.getIsActive())
				userEntity.setIsActive(request.getIsActive());
			if (null != request.getIsDelete())
				userEntity.setIsDelete(request.getIsDelete());
			if (null != request.getDownloadLimit())
				userEntity.setDownloadLimit(request.getDownloadLimit());
			if (null != request.getMemberId())
				userEntity.setMemberId(request.getMemberId());
			userEntity.setModifiedDate(new Date());
			userEntity.setModifiedBy(accessedBy);
			userEntity = userRepository.save(userEntity);
		}
	}

	public void userDelete(UserRequest request, Long userId, Long accessedBy) throws Exception {
		User userEntity = userRepository.findById(userId).get();
		if (userEntity == null) {
			throw new ServiceException("User is not registered");
		} else {
			// User userEntity = list.get();

			if (null != request.getIsDelete())
				userEntity.setIsDelete(request.getIsDelete());
			userEntity.setModifiedDate(new Date());
			userEntity.setModifiedBy(accessedBy);
			userEntity = userRepository.save(userEntity);

			if (userEntity != null) {

				List<UserSubscription> srcList = userSubscriptionRepository
						.findAllByUserIdAndIsActiveOrderByIdDesc(accessedBy, "Y");

				if (!srcList.isEmpty()) {
					UserSubscription actSubs = srcList.get(0);
					Integer count = Integer.parseInt(actSubs.getSubUserCount());
					actSubs.setSubUserCount("" + (count + 1));
					userSubscriptionRepository.save(actSubs);
				}

			}

		}
	}

	public UserDetailsResponse userDetails(Long userId) throws Exception {

		UserDetailsResponse userDetailsResponse = new UserDetailsResponse();

		User userEntity = userRepository.findById(userId).get();
		if (userEntity != null) {
			userDetailsResponse.setId(userEntity.getId());
			userDetailsResponse.setUserId(userEntity.getId());
			userDetailsResponse.setFirstname(userEntity.getFirstname());
			userDetailsResponse.setLastname(userEntity.getLastname());
			userDetailsResponse.setEmail(userEntity.getEmail());
			userDetailsResponse.setMobile(userEntity.getMobile());
			userDetailsResponse.setPassword(userEntity.getPassword());
			userDetailsResponse.setCompanyName(userEntity.getCompanyName());
			userDetailsResponse.setUplineId(userEntity.getUplineId());
			userDetailsResponse.setIsActive(userEntity.getIsActive());
			userDetailsResponse.setDownloadLimit(userEntity.getDownloadLimit());
			userDetailsResponse.setMemberId(userEntity.getMemberId());
		}
		return userDetailsResponse;
	}

	public UserListResponse userList(Long uplineId, String userType, Long accessedBy, String isExpired, String isActive)
			throws Exception {

		UserListResponse userListResponse = new UserListResponse();

		List<User> srcList = null;

		if (uplineId != null) {
			srcList = userRepository.findAllByIsDeleteAndUplineIdOrderByIdDesc("N", uplineId);
		} else if (userType != null && isExpired == null && isActive == null) {
			srcList = userRepository.findAllByIsDeleteAndUserTypeOrderByIdDesc("N", userType);
		} else if (isExpired != null && userType != null && isActive == null) {
			srcList = userRepository.findAllExpiredUsers();
		} else if (userType != null && isActive != null && isExpired == null) {
			srcList = this.userRepository.findAllByIsActive(isActive);
		} else {
			srcList = userRepository.findAllByIsDeleteOrderByIdDesc("N");
		}

		// List<User> srcList = userRepository.findAll();

		List<UserDetailsResponse> targetList = new ArrayList<UserDetailsResponse>();

		if (null != srcList && !srcList.isEmpty()) {

			for (User user : srcList) {
				UserDetailsResponse userDetails = new UserDetailsResponse();
				BeanUtils.copyProperties(user, userDetails);

				List<UserSubscription> srcList1 = userSubscriptionRepository
						.findAllByUserIdAndIsActiveOrderByIdDesc(user.getId(), "Y");
				if (null != srcList1) {

					for (UserSubscription userSubscription : srcList1) {
						userDetails.setSubscriptionExpiredDate(userSubscription.getAccountExpireDate());
					}
				}

				targetList.add(userDetails);
			}
		}

		userListResponse.setUserList(targetList);

		return userListResponse;
	}

	public void createUserSubscription(@Valid UserSubscriptionRequest userSubscriptionRequest, Long accessedBy) {

		// First In-Active all Previous subscriptions Start

		List<UserSubscription> srcList = userSubscriptionRepository
				.findAllByUserIdOrderByIdDesc(userSubscriptionRequest.getUserId());

		if (null != srcList && !srcList.isEmpty()) {

			for (UserSubscription userSubscription : srcList) {

				UserSubscription userSubscriptionEntity = new UserSubscription();

				userSubscriptionEntity.setId(userSubscription.getId());
				userSubscriptionEntity.setIsActive("N");

				userSubscriptionEntity.setUserId(userSubscription.getUserId());
				userSubscriptionEntity.setSubscriptionId(userSubscription.getSubscriptionId());
				userSubscriptionEntity.setName(userSubscription.getName());
				userSubscriptionEntity.setDescription(userSubscription.getDescription());
				userSubscriptionEntity.setPrice(userSubscription.getPrice());
				userSubscriptionEntity.setValidityInDay(userSubscription.getValidityInDay());
				userSubscriptionEntity.setCountries(userSubscription.getCountries());
				userSubscriptionEntity.setContinent(userSubscription.getContinent());
				userSubscriptionEntity.setDownloadLimit(userSubscription.getDownloadLimit());
				userSubscriptionEntity.setTotalWorkspace(userSubscription.getTotalWorkspace());
				userSubscriptionEntity.setSubUserCount(userSubscription.getSubUserCount());
				userSubscriptionEntity.setDataAccessInMonth(userSubscription.getDataAccessInMonth());
				userSubscriptionEntity.setDownloadPerDay(userSubscription.getDownloadPerDay());
				userSubscriptionEntity.setSupport(userSubscription.getSupport());
				userSubscriptionEntity.setTicketManager(userSubscription.getTicketManager());
				userSubscriptionEntity.setRecordsPerWorkspace(userSubscription.getRecordsPerWorkspace());
				userSubscriptionEntity.setDisplayFields(userSubscription.getDisplayFields());
				userSubscriptionEntity.setQueryPerDay(userSubscription.getQueryPerDay());
				userSubscriptionEntity.setAccountExpireDate(userSubscription.getAccountExpireDate());
				userSubscriptionEntity.setCreatedBy(userSubscription.getCreatedBy());
				userSubscriptionEntity.setCreatedDate(userSubscription.getCreatedDate());

				userSubscriptionEntity.setAllowedChapter(userSubscription.getAllowedChapter());
				userSubscriptionEntity.setDataAccessUpto(userSubscription.getDataAccessUpto());

				userSubscriptionEntity.setModifiedBy(accessedBy);
				userSubscriptionEntity.setModifiedDate(new Date());
				userSubscriptionRepository.save(userSubscriptionEntity);

			}
		}

		// In-Active all Previous subscriptions End

		UserSubscription userSubscriptionEntity = new UserSubscription();
		userSubscriptionEntity.setUserId(userSubscriptionRequest.getUserId());
		userSubscriptionEntity.setSubscriptionId(userSubscriptionRequest.getSubscriptionId());

		// Fetch Subscription Data From Master Data
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("accessedBy", "" + userSubscriptionRequest.getUserId());
		headers.add("Authorization", "Basic YXBpLWV4aW13YXRjaDp1ZTg0Q1JSZnRAWGhBMyRG");

		/*
		 * ResponseEntity<Subscription> responseEntity = restTemplate
		 * .getForEntity(subscriptionUrl + userSubscriptionRequest.getSubscriptionId(),
		 * Subscription.class);
		 */
		ResponseEntity<Subscription> responseEntity = restTemplate.exchange(
				subscriptionUrl + userSubscriptionRequest.getSubscriptionId(), HttpMethod.GET,
				new HttpEntity<Object>(headers), Subscription.class);
		Subscription subscription = responseEntity.getBody();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, subscription.getValidityInDay());
		userSubscriptionEntity.setName(subscription.getName());
		userSubscriptionEntity.setDescription(subscription.getDescription());
		userSubscriptionEntity.setPrice(subscription.getPrice());
		userSubscriptionEntity.setValidityInDay(subscription.getValidityInDay());
		userSubscriptionEntity.setCountries(subscription.getCountryId().toString());
		userSubscriptionEntity.setContinent(subscription.getContinentId().toString());
		userSubscriptionEntity.setDownloadLimit(subscription.getDownloadLimit());
		userSubscriptionEntity.setTotalWorkspace(subscription.getWorkspaceLimit());
		userSubscriptionEntity.setSubUserCount(subscription.getSubUserCount());
		userSubscriptionEntity.setDataAccessInMonth(subscription.getDataAccess());
		userSubscriptionEntity.setDownloadPerDay(subscription.getMaxDownloadPerDay());
		userSubscriptionEntity.setSupport(subscription.getSupport());
		userSubscriptionEntity.setTicketManager(subscription.getTicketManager());
		userSubscriptionEntity.setRecordsPerWorkspace(subscription.getRecordPerWorkspace());
		userSubscriptionEntity.setDisplayFields(subscription.getDisplayFields());
		userSubscriptionEntity.setQueryPerDay(subscription.getSearchQueryPerDay());

		userSubscriptionEntity.setAllowedChapter(subscription.getAllowedChapter());

		userSubscriptionEntity.setAccountExpireDate(cal.getTime());
		if (null != userSubscriptionRequest.getIsActive())
			userSubscriptionEntity.setIsActive(userSubscriptionRequest.getIsActive());
		else
			userSubscriptionEntity.setIsActive("Y");
		userSubscriptionEntity.setCreatedBy(accessedBy);
		userSubscriptionEntity.setCreatedDate(new Date());
		userSubscriptionRepository.save(userSubscriptionEntity);

		if (null == srcList || srcList.isEmpty()) {
			userWelcomeEmail(userSubscriptionRequest.getUserId(), subscription.getName(), cal.getTime().toString());
		}

	}

	private boolean userWelcomeEmail(Long userId, String subscriptionName, String accountExpireDate) {
		try {
			String msgBody = "";
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");

			User userEntity = userRepository.findById(userId).get();

			Session mailSession = Session.getDefaultInstance(props);
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);

			message.setSubject("Activation Success For BE Datos Online Platform");

			msgBody = "Dear " + userEntity.getFirstname()
					+ ",<br><br>Thank you for choosing BE Datos online Platform and  Find below activation Details to help you get started.<br><br>";
			msgBody = msgBody
					+ "As This platform will help you to approach your business objectives with greater intelligence.<br><br>";

			msgBody = msgBody + "<b>Scope of Your Package:</b><br><br>";
			msgBody = msgBody + "<b>Username:</b> " + userEntity.getEmail() + "<br>";
			msgBody = msgBody + "<b>Password:</b> " + userEntity.getPassword() + "<br>";
			msgBody = msgBody + "<b>Subscription Plan:</b> " + subscriptionName + "<br>";
			msgBody = msgBody + "<b>Subscription Expire Date:</b> " + accountExpireDate + "<br>";
			msgBody = msgBody + "<b>Link:</b> https://app.eximwatch.com/login<br><br><br>";

			msgBody = msgBody
					+ "If you have any questions or need assistance, feel free to reach out to our customer support team at (+91) 9073371811/9051062400.<br><br>";
			msgBody = msgBody
					+ "We're excited to embark on this journey with warm welcome and look forward to seeing you thrive on our web based Portal.<br><br><br>";
			msgBody = msgBody + "Best regards,<br><br>";
			msgBody = msgBody + "BE Datos Team";

			message.setContent(msgBody, "text/html");
			Address[] from = InternetAddress.parse("accounts@eximwatch.com");// Your domain email
			message.addFrom(from);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEntity.getEmail())); // Send email To
																										// (Type email
																										// ID that you
																										// want to send)

			transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean subUserWelcomeEmail(Long userId, Long uplineid) {
		try {
			String msgBody = "";
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");

			User userEntity = userRepository.findById(userId).get();

			@SuppressWarnings("unused")
			UserSubscriptionList userSubscriptionList = new UserSubscriptionList();

			List<UserSubscription> srcList = userSubscriptionRepository
					.findAllByUserIdAndIsActiveOrderByIdDesc(uplineid, "Y");

			Session mailSession = Session.getDefaultInstance(props);
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);

			message.setSubject("Sub User Login ID Created for BE Datos Online Platform");

			msgBody = "Dear " + userEntity.getFirstname()
					+ ",<br><br>Your Sub User Login ID details Below mentioned Here.<br><br>";

			msgBody = msgBody + "<b>Username:</b> " + userEntity.getEmail() + "<br>";
			msgBody = msgBody + "<b>Password:</b> " + userEntity.getPassword() + "<br>";

			@SuppressWarnings("unused")
			List<UserSubscriptionDetails> targetList = new ArrayList<UserSubscriptionDetails>();

			if (null != srcList) {

				for (UserSubscription userSubscription : srcList) {

					msgBody = msgBody + "<b>Subscription Plan:</b> " + userSubscription.getName() + "<br>";
					msgBody = msgBody + "<b>Subscription Expire Date:</b> " + userSubscription.getAccountExpireDate()
							+ "<br>";
				}
			}

			msgBody = msgBody + "<b>Link:</b> https://app.eximwatch.com/login<br><br><br>";

			msgBody = msgBody
					+ "If you have any questions or need assistance, feel free to reach out to our customer support team at (+91) 9073371811/9051062400.<br><br>";
			msgBody = msgBody
					+ "We're excited to embark on this journey with warm welcome and look forward to seeing you thrive on our web based Portal.<br><br><br>";
			msgBody = msgBody + "Best regards,<br><br>";
			msgBody = msgBody + "BE Datos Team";

			message.setContent(msgBody, "text/html");
			Address[] from = InternetAddress.parse("accounts@eximwatch.com");// Your domain email
			message.addFrom(from);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEntity.getEmail())); // Send email To
																										// (Type email
																										// ID that you
																										// want to send)

			transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void userLogout(@Valid LogoutRequest logoutRequest, Long accessedBy) throws Exception {
		Optional<LoginDetails> list = loginDetailsRepository.findById(logoutRequest.getLoginId());
		String proeName = null;
		Connection connection = null;

		try {
			connection = jdbcTemplate.getDataSource().getConnection();

			if (list == null) {
				throw new ServiceException("User is not registered");
			} else {

				proeName = QueryConstant.userLogout;
				CallableStatement callableStatement = connection.prepareCall(proeName);
				callableStatement.setLong(1, accessedBy);
				callableStatement.execute();

				LoginDetails loginDetailsEntity = list.get();
				loginDetailsEntity.setLogoutTime(new Date());
				loginDetailsRepository.save(loginDetailsEntity);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			if (connection != null)
				connection.close();
		}

	}

//	public LoginListResponse loginList(Long userId,Long uplineId, Long accessedBy) throws Exception{
//		
//		LoginListResponse loginListResponse = new LoginListResponse();
//		
//		List<LoginDetails> srcList = null;
//		
//		if(userId != null)
//		{
//			srcList = loginDetailsRepository.findByUserIdOrderByIdDesc(userId);
//		}
//		else if(uplineId != null)
//		{
//			srcList = loginDetailsRepository.findByUplineIdOrderByIdDesc(uplineId);
//		}
//		else
//		{
//			srcList = loginDetailsRepository.findAllByOrderByIdDesc();
//		}
//		
//		List<LoginList> targetList = new ArrayList<LoginList>();
//		
//		if(null!=srcList) {
//			
//			for(LoginDetails loginDetails:srcList) {
//				LoginList  loginList= new LoginList();
//				BeanUtils.copyProperties(loginDetails, loginList);	
//				
//				User userEntity = userRepository.findById(loginDetails.getUserId()).get();
//				
//				loginList.setEmail(userEntity.getEmail());
//				loginList.setMobile(userEntity.getMobile());
//				loginList.setName(userEntity.getFirstname()+" "+userEntity.getLastname());
//				
//				
//				targetList.add(loginList);
//			}
//		}
//		
//		loginListResponse.setLoginList(targetList);
//		
//		
//		return loginListResponse;
//		
//	}

	public LoginListResponse loginList(Long userId, Long uplineId, int pageNumber, Long accessedBy, Date fromDate,
			Date toDate) throws Exception {

		LoginListResponse loginListResponse = new LoginListResponse();
		if (pageNumber > 0)
			pageNumber--;
		Pageable pageable = PageRequest.of(pageNumber, 20, Sort.by("id").descending());

		List<LoginDetails> srcList = null;

		if (userId != null) {
			if (fromDate != null)
				srcList = loginDetailsRepository.findByUserIdAndDateRange(userId, pageable.getPageNumber(),
						pageable.getPageSize(), fromDate, toDate);// D1
			else
				srcList = loginDetailsRepository.findByUserId(userId, pageable);
		} else if (uplineId != null) {
			if (fromDate != null)
				srcList = loginDetailsRepository.findByUplineIdAndDateRangeOrderByIdDesc(uplineId,
						pageable.getPageNumber(), pageable.getPageSize(), fromDate, toDate);// D2
			else
				srcList = loginDetailsRepository.findByUplineIdOrderByIdDesc(uplineId, pageable.getPageNumber(),
						pageable.getPageSize());
		} else {
			if (fromDate != null)
				srcList = loginDetailsRepository.findAllByDateRange(pageable.getPageNumber(), pageable.getPageSize(),
						fromDate, toDate);// D3
			else
				srcList = loginDetailsRepository.findAll(pageable).getContent();
		}

		List<LoginList> targetList = new ArrayList<LoginList>();

		if (null != srcList) {

			for (LoginDetails loginDetails : srcList) {
				LoginList loginList = new LoginList();
				BeanUtils.copyProperties(loginDetails, loginList);

				User userEntity = userRepository.findById(loginDetails.getUserId()).get();

				loginList.setEmail(userEntity.getEmail());
				loginList.setMobile(userEntity.getMobile());
				loginList.setName(userEntity.getFirstname() + " " + userEntity.getLastname());

				targetList.add(loginList);
			}
		}

		loginListResponse.setLoginList(targetList);

		return loginListResponse;

	}

	public Long loginListCount(Long userId, Long uplineId, Long accessedBy, Date fromDate, Date toDate)
			throws Exception {

		Long count = null;

		if (userId != null) {
			if (fromDate != null)
				count = loginDetailsRepository.countByUserIdAndDateRange(userId, fromDate, toDate);// DC1
			else
				count = loginDetailsRepository.countByUserId(userId);
		} else if (uplineId != null) {
			if (fromDate != null)
				count = loginDetailsRepository.countByUplineIdAndDateRange(uplineId, fromDate, toDate);// DC2
			else
				count = loginDetailsRepository.countByUplineIdCustom(uplineId);
		} else {
			if (fromDate != null)
				count = loginDetailsRepository.countByDateRange(fromDate, toDate);// DC3
			else
				count = loginDetailsRepository.count();
		}

		return count;

	}

	public UserSubscriptionList userSubscriptionList(Long userId, Long accessedBy) {

		UserSubscriptionList userSubscriptionList = new UserSubscriptionList();

		List<UserSubscription> srcList = userSubscriptionRepository.findAllByUserIdOrderByIdDesc(userId);

		List<UserSubscriptionDetails> targetList = new ArrayList<UserSubscriptionDetails>();

		if (null != srcList) {

			for (UserSubscription userSubscription : srcList) {
				UserSubscriptionDetails userSubscriptionDetails = new UserSubscriptionDetails();
				BeanUtils.copyProperties(userSubscription, userSubscriptionDetails);
				userSubscriptionDetails.setCountries(convertStringToList(userSubscription.getCountries()));
				userSubscriptionDetails.setContinent(convertStringToList(userSubscription.getContinent()));
				targetList.add(userSubscriptionDetails);
			}
		}

		userSubscriptionList.setUserSubscriptionList(targetList);

		return userSubscriptionList;
	}

	private List<String> convertStringToList(String s1) {
		if (s1 != null) {
			// String s1="[a,b,c,d]";
			String replace = s1.replace("[", "");
			System.out.println(replace);
			String replace1 = replace.replace("]", "");
			System.out.println(replace1);
			List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
			System.out.println(myList.toString());
			return myList;
		}
		return null;

	}

	public UserSubscriptionList activeUserSubscriptionList(Long userId, Long accessedBy) {
		// TODO Auto-generated method stub
		UserSubscriptionList userSubscriptionList = new UserSubscriptionList();

		List<UserSubscription> srcList = userSubscriptionRepository.findAllByUserIdAndIsActiveOrderByIdDesc(userId,
				"Y");

		List<UserSubscriptionDetails> targetList = new ArrayList<UserSubscriptionDetails>();

		if (null != srcList) {

			for (UserSubscription userSubscription : srcList) {
				UserSubscriptionDetails userSubscriptionDetails = new UserSubscriptionDetails();
				BeanUtils.copyProperties(userSubscription, userSubscriptionDetails);
				userSubscriptionDetails.setCountries(convertStringToList(userSubscription.getCountries()));
				userSubscriptionDetails.setContinent(convertStringToList(userSubscription.getContinent()));
				targetList.add(userSubscriptionDetails);
			}
		}

		userSubscriptionList.setUserSubscriptionList(targetList);

		return userSubscriptionList;
	}

	public void updateUserSubscription(UserSubscriptionDetailsRequest userSubscriptionDetailsRequest,
			Long userSubscriptionId, Long accessedBy) throws Exception {

		UserSubscription userSubscriptionEntity = userSubscriptionRepository.findById(userSubscriptionId).get();
		if (userSubscriptionEntity == null) {
			throw new ServiceException("Incorrect User Subscription");
		} else {

			if (null != userSubscriptionDetailsRequest.getDownloadLimit())
				userSubscriptionEntity.setDownloadLimit(userSubscriptionDetailsRequest.getDownloadLimit());
			if (null != userSubscriptionDetailsRequest.getTotalWorkspace())
				userSubscriptionEntity.setTotalWorkspace(userSubscriptionDetailsRequest.getTotalWorkspace());
			if (null != userSubscriptionDetailsRequest.getSubUserCount())
				userSubscriptionEntity.setSubUserCount(userSubscriptionDetailsRequest.getSubUserCount());
			if (null != userSubscriptionDetailsRequest.getDataAccessInMonth())
				userSubscriptionEntity.setDataAccessInMonth(userSubscriptionDetailsRequest.getDataAccessInMonth());
			if (null != userSubscriptionDetailsRequest.getDownloadPerDay())
				userSubscriptionEntity.setDownloadPerDay(userSubscriptionDetailsRequest.getDownloadPerDay());
			if (null != userSubscriptionDetailsRequest.getSupport())
				userSubscriptionEntity.setSupport(userSubscriptionDetailsRequest.getSupport());
			if (null != userSubscriptionDetailsRequest.getTicketManager())
				userSubscriptionEntity.setTicketManager(userSubscriptionDetailsRequest.getTicketManager());
			if (null != userSubscriptionDetailsRequest.getRecordsPerWorkspace())
				userSubscriptionEntity.setRecordsPerWorkspace(userSubscriptionDetailsRequest.getRecordsPerWorkspace());
			if (null != userSubscriptionDetailsRequest.getDisplayFields())
				userSubscriptionEntity.setDisplayFields(userSubscriptionDetailsRequest.getDisplayFields());
			if (null != userSubscriptionDetailsRequest.getQueryPerDay())
				userSubscriptionEntity.setQueryPerDay(userSubscriptionDetailsRequest.getQueryPerDay());
			if (null != userSubscriptionDetailsRequest.getAccountExpireDate())
				userSubscriptionEntity.setAccountExpireDate(userSubscriptionDetailsRequest.getAccountExpireDate());
			if (null != userSubscriptionDetailsRequest.getAllowedChapter())
				userSubscriptionEntity.setAllowedChapter(userSubscriptionDetailsRequest.getAllowedChapter());
			if (null != userSubscriptionDetailsRequest.getDataAccessUpto())
				userSubscriptionEntity.setDataAccessUpto(userSubscriptionDetailsRequest.getDataAccessUpto());
			if (null != userSubscriptionDetailsRequest.getCountryId())
				userSubscriptionEntity.setCountries(userSubscriptionDetailsRequest.getCountryId().toString());

			userSubscriptionEntity.setModifiedDate(new Date());
			userSubscriptionEntity.setModifiedBy(accessedBy);
			userSubscriptionEntity = userSubscriptionRepository.save(userSubscriptionEntity);
		}

	}

	public LoginList loginStatus(Long loginId, Long accessedBy) {

		LoginList loginList = new LoginList();

		Optional<LoginDetails> srcList = null;

		srcList = loginDetailsRepository.findById(loginId);

		loginList.setId(srcList.get().getId());
		loginList.setLoginTime(srcList.get().getLoginTime());
		loginList.setLogoutTime(srcList.get().getLogoutTime());

		return loginList;
	}

	public List<LoginList> currentLoginUsers(Long accessedBy) throws ParseException {
		List<LoginList> loginList = new ArrayList<LoginList>();

		String localDate = LocalDate.now().toString();
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(localDate + " 00:00:00.000");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(localDate + " 23:59:59.999");

		List<LoginDetails> list = this.loginDetailsRepository.allLoginUsers(fromDate, toDate);

		if (list.size() > 0) {
			for (LoginDetails loginuser : list) {
				LoginList user1 = new LoginList();

				User user = userRepository.findById(loginuser.getUserId()).get();
				user1.setUserId(user.getId());
				user1.setName(user.getFirstname() + " " + user.getLastname());
				user1.setEmail(user.getEmail());
				user1.setMobile(user.getMobile());
				user1.setCompanyName(user.getCompanyName());
				user1.setSessionId(loginuser.getSessionId());
				user1.setIpAddress(loginuser.getIpAddress());
				user1.setLoginTime(loginuser.getLoginTime());
				loginList.add(user1);
			}
		}

		return loginList;
	}

	public Long currentLoginUserCount(Long accessedBy) throws ParseException {

		String localDate = LocalDate.now().toString();
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(localDate + " 00:00:00.000");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(localDate + " 23:59:59.999");

		Long count = this.loginDetailsRepository.countAllLoginUsers(fromDate, toDate);

		return count;
	}

}
