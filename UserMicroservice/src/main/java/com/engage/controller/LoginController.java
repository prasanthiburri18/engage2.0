package com.engage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.engage.commons.dto.PathwayPatientBlockDto;
import com.engage.commons.dto.PatientDto;
import com.engage.commons.dto.PatientPathwayBlockNotFoundException;
import com.engage.commons.exception.ConstraintViolationException;
import com.engage.commons.validators.utils.ConstraintValidationUtils;
import com.engage.dao.OrganizationDao;
import com.engage.dao.UserDao;
import com.engage.model.Organization;
import com.engage.model.User;
import com.engage.service.UserService;
import com.engage.util.AdvancedEncryptionStandard;
import com.engage.util.JsonMessage;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
/**
 * User MicroServices for User API Operations User Authentication Methods
 * 
 * @author StartUP Labs
 * @version 1.0
 * 
 */
public class LoginController {

	/**
	 * Class' LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	@Value("${emailMicroService.URL}")
	private String emailMicroserviceURL;
	@Value("${portal.URL}")
	private String portalURL;

	@Value("${patientMicroserviceUrl}")
	private String patientMicroserviceUrl;

	@Autowired
	private OAuth2RestTemplate restTemplate;

	// private RestTemplate restTemplate = new RestTemplate();
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao _userDao;
	
	@Autowired
	private OrganizationDao _organizationDao;

	private ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * Used for validating model objects
	 */
	@Autowired
	private Validator validator;

	/**
	 * For encrypting password. Removed previous encoder.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * User Registration Method
	 * 
	 * @Inputparam user
	 * @return JsonObject
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public @ResponseBody JsonMessage create(@RequestBody User user) {
		JsonMessage response = new JsonMessage();
		try { // Engage2.0
			if (user != null) {
				user.setUserType("A");
				user.setStatus("N");
			}
			Set<ConstraintViolation<User>> violations = validator.validate(user);
			if (!violations.isEmpty()) {
				LOGGER.info("Checking validation for user");
				Map<String, String> errormessages = ConstraintValidationUtils.getMapOfValidations(violations);
				if (user.getPassword() == null || user.getPassword().trim().equals("")) {
					LOGGER.info("Server side validation for password");
					errormessages.put("password", "Password cannot be blank");
				}
				throw new ConstraintViolationException(errormessages);
			}
			// Engage 2.0
			if ((_userDao.getByEmail(user.getEmail())).size() > 0) {
				LOGGER.warn("Email already exists");
				response.setMessage("Email already exists.");
				response.setStatuscode(208);
				return response;
			} else {

				userService.register(user);
/*				// final String simpleMailUrl = emailMicroserviceURL +
				// "/email/send";
				// restTemplate.postForObject(simpleMailUrl, data1,
				// String.class);
				LOGGER.info("Saving Organization..");
				Organization org = new Organization();
				org.setName(user.getPracticeName());

				Integer orgid = _organizationDao.save(org);
				user.setOrgid(orgid);

				user.setPassword(passwordEncoder.encode(user.getPassword()));

				Timestamp timestamp = new Timestamp(System.currentTimeMillis());

				user.setCreateDate(timestamp);
				user.setUpdateDate(timestamp);
				LOGGER.info("Registering new user");
				BigInteger id = _userDao.save(user);

				UserRoles userRoles = new UserRoles();
				userRoles.setUserId(id);
				userRoles.setRoleId(1);
				LOGGER.info("Before saving Roles");
				_userRolesDao.save(userRoles);
				
				LOGGER.info("Checking whether user stored into database..");
				
				
				Map<String, Object> data1 = new HashMap<String, Object>();
				data1.put("from", "EngageApp<support@quantifiedcare.com>");
				data1.put("to", user.getEmail());
				data1.put("subject", "Account Confirmation");
				data1.put("text",
						"Hi <b>" + user.getFullName()
								+ ",</b><br><br>Congratulations! Your account has been created. Please click on the link to verify your email address and start using Engage.<br><br><a href='"
								+ portalURL + "/userconfirmation.html?keyconfirm="
								+ AdvancedEncryptionStandard.encrypt(user.getEmail())
								+ "'>Verify</a><br><br>Thank You,<br>Team Engage at Quantified Care");
				data1.put("status", true);

				userService.sendEmail(data1);
				*/
				response.setMessage("User registered successfully");
				response.setStatuscode(200);
				return response;
			}
		} catch (ConstraintViolationException ex) {
			response.setData(ex.getJsonObject().toString());
			response.setStatuscode(400);
			response.setMessage("Invalid team member format");
			return response;
		} catch (Exception ex) {
			LOGGER.info("Registration failed due to "+ex.getMessage());
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * Loading Patient Pathway Block anonymous call
	 * 
	 * @Inputparam user
	 * @return JsonObject
	 */

	@RequestMapping(value = "/getPatientpathwayblockbyId", method = RequestMethod.POST)
	public @ResponseBody JsonMessage getPatientpathwayblockbyId(@RequestBody Map<String, String> json) {

		JsonMessage response = null;
		try {
			Integer rid = Integer.parseInt(json.get("id").toString());
			
			String dateOfBirth = json.get("dob");
			//List results = _userDao.getPatientpathwayblockById(rid);
			List<PatientDto> patientListOfdob = userService.getPatientsListByDob(dateOfBirth);
			
			
			
			 JsonMessage response1 = userService.getPatientpathwayblockById(rid);
			
			
			List<LinkedHashMap<String, Object>> mapList = (List<LinkedHashMap<String, Object>>) response1.getData();
			List<PathwayPatientBlockDto> pathwayBlockMapped = new ArrayList<>();
		
			mapList.forEach(p->pathwayBlockMapped.add(objectMapper.convertValue(p, PathwayPatientBlockDto.class)));
			
	//		pathwayPatientBlock.forEach(p->pathwayBlockMapped.add(objectMapper.convertValue(p, PathwayPatientBlockDto.class)));
			boolean isValidDateOfBirth = false;
			
			if(patientListOfdob!=null&&patientListOfdob.size()>0){
			for(PathwayPatientBlockDto ppbd : pathwayBlockMapped){
				if(!patientListOfdob.stream().noneMatch(p->p.getId()==ppbd.getPatientId())){
					isValidDateOfBirth=true;
				}
					
			}}
			if(!isValidDateOfBirth){
				throw new PatientPathwayBlockNotFoundException("Not a valid block id / date of birth");
			}
			
			return response1;

		} catch (Exception ex) {
			response = new JsonMessage();
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;

		}

	}

	/**
	 * Loading Patient by DOB anonymous call
	 * 
	 * @Inputparam user
	 * @return JsonObject
	 */

	@RequestMapping(value = "/getPatientBydob", method = RequestMethod.POST)
	public @ResponseBody JsonMessage getPatientBydob(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			String pdob = json.get("dob");
			LOGGER.info("Date of birth of patient: "+pdob);
			// int result = _userDao.verifyPatientInfobydob(pdob);
			int result = userService.verifyPatientInfobydob(pdob);
			response.setData(result);
			response.setStatuscode(200);
			return response;
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response;
		}

	}

	/**
	 * Patient Reply Call Back for Twilio (anonymous call)
	 * 
	 * @Inputparam user
	 * @return JsonObject
	 */
	@RequestMapping(value = "/userreply", method = RequestMethod.POST)
	public ResponseEntity<String> receiveBody(@RequestParam("From") String From, @RequestParam("Body") String Body) {
		JsonMessage response = new JsonMessage();
		JsonMessage response1 = new JsonMessage();
		HttpHeaders responseHeaders = new HttpHeaders();
		String fromuser = From;
		String userbody = Body;
		String pexist = "no";
		String resmessage = "";
		try {

			if (userbody.equals("Y")) {

				RestTemplate restTemplate = new RestTemplate();
				Map<String, Object> data1 = new HashMap<String, Object>();
				data1.put("From", fromuser);
				data1.put("Body", userbody);
				// String ph=fromuser.substring(3);//for India/
				String ph = fromuser.substring(2);// for US/

				//ArrayList patients = _userDao.patientidbyphone(ph);
				ArrayList patients = userService.patientidbyphone(ph);
				Iterator itr = patients.iterator();

				while (itr.hasNext()) {
					Object element = itr.next();

					//if (_userDao.verifyPatientInfo(Long.parseLong(element.toString())) > 0) {
					if (userService.verifyPatientInfo(Long.parseLong(element.toString())) > 0) {
					pexist = "yes";

					} else {

					}

				}
				if (pexist.equals("yes")) {
					resmessage = "Hi there! At this time we are not monitoring our mailbox and your response will not be received. For any questions or concerns contact your Transition Specialist at 1-800-981-5904";

				}
				if (pexist.equals("no")) {

					resmessage = "Thank you for joining. You will start receiving messages.";

				}
				String res = restTemplate.postForObject(patientMicroserviceUrl + "/api/v1/patientreply", data1,
						String.class);

				JsonFactory factory = new JsonFactory();
				JsonParser parser = factory.createParser(res);
				while (!parser.isClosed()) {
					JsonToken token = parser.nextToken();
					if (JsonToken.FIELD_NAME.equals(token) && "message".equals(parser.getCurrentName())) {
						token = parser.nextToken();
						resmessage = parser.getText();
						// System.out.println(resmessage);
					}
				}
				return new ResponseEntity<String>(resmessage, responseHeaders, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(
						"Hi there! At this time we are not monitoring our mailbox and your response will not be received. For any questions or concerns contact your Transition Specialist at 1-800-981-5904",
						responseHeaders, HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<String>(
					"Hi there! At this time we are not monitoring our mailbox and your response will not be received. For any questions or concerns contact your Transition Specialist at 1-800-981-5904",
					responseHeaders, HttpStatus.OK);
		}
	}

	/**
	 * 
	 * Organization Create Call
	 * 
	 * @Inputparam organization JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/organizationcreate", method = RequestMethod.POST)
	public @ResponseBody JsonMessage organizationcreate(@RequestBody final Organization organization) {
		JsonMessage response = new JsonMessage();
		try {

			Integer orgid = _organizationDao.save(organization);
			response.setMessage("User registred successfully");
			response.setData(orgid);
			response.setStatuscode(200);
			return response;

		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * 
	 * Check for email existence
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/IsEmailExists", method = RequestMethod.POST)
	public @ResponseBody JsonMessage IsEmailExists(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			if ((_userDao.getByEmail(json.get("emailid")).size() > 0)) {
				response.setMessage("Email Already Exists");
				response.setStatuscode(200);
				return response;
			} else {
				response.setMessage("No profile information found.");
				response.setStatuscode(204);
				return response;
			}
		} catch (Exception ex) {
			response.setMessage("No profile information found.");
			response.setStatuscode(204);
			return response;
		}
	}

	/**
	 * 
	 * Verify User email existence
	 * 
	 * @Inputparam organization JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/verify_email", method = RequestMethod.POST)
	public @ResponseBody JsonMessage verifyEmail(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			if (!(_userDao.getByEmail(AdvancedEncryptionStandard.decrypt(json.get("emailid"))).size() > 0)) {
				response.setMessage("Invalid account.");
				response.setStatuscode(204);
				return response;
			} else {
				User user = _userDao.getById(AdvancedEncryptionStandard.decrypt(json.get("emailid")));
				user.setStatus("Y");
				_userDao.update(user);
				response.setMessage("Account activated successfully.");
				response.setStatuscode(200);
				return response;
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * 
	 * Verify Forgot Password Method
	 * 
	 * @Inputparam organization JsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/verify_emailForgetpwd", method = RequestMethod.PUT)
	public @ResponseBody JsonMessage resetPassword(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			if ((_userDao.getByEmail(AdvancedEncryptionStandard.decrypt(json.get("emailid"))).size() > 0)) {
				User user = _userDao.getById(AdvancedEncryptionStandard.decrypt(json.get("emailid")));
				// user.setPassword(AdvancedEncryptionStandard.encrypt(json.get("password")));
				// Engage2.0 change
				user.setPassword(passwordEncoder.encode(json.get("password")));

				_userDao.update(user);
				response.setMessage("Password updated successfully.");
				response.setStatuscode(200);
				return response;
			} else {
				response.setMessage("Email not registered.");
				response.setStatuscode(204);
				return response;
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatuscode(203);
			return response;
		}

	}

	/**
	 * 
	 * Load Org names Method
	 * 
	 * @Inputparam JsonObject
	 * @return JsonObject
	 */

	@RequestMapping(value = "/getAllPracticeNames", method = RequestMethod.GET)
	public @ResponseBody JsonMessage getAllPraticeNames() {// (@RequestBody
															// Map<String,
															// String> json) {
		JsonMessage response = new JsonMessage();
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("praticenames", _userDao.getallPraticeNames());
			response.setMessage("List of Praticenames.");
			response.setData(data);
			response.setStatuscode(200);
			return response;

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatuscode(203);
			return response;
		}
	}

	/**
	 * 
	 * User Forgot API Call
	 * 
	 * @Inputparam organization JsonObject
	 * @return JsonObject
	 */
	@RequestMapping(value = "/forget_password", method = RequestMethod.POST)
	public @ResponseBody JsonMessage forgetPassword(@RequestBody Map<String, String> json) {
		JsonMessage response = new JsonMessage();
		try {
			if ((_userDao.getByEmail(json.get("emailid")).size() > 0)) {
				User user = _userDao.getById(json.get("emailid"));
				response.setMessage("Password sent to your email.");
				response.setStatuscode(200);
				// RestTemplate restTemplate = new RestTemplate();
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("from", "EngageApp<bhanu735@gmail.com>");
				data.put("to", user.getEmail());
				data.put("subject", "Forgot Password");
				data.put("text",
						"Dear <b>" + user.getFullName()
								+ ",</b><br><br>Please click on the link below to reset your password.<br><br><a href='"
								+ portalURL + "/forgotkeyconfirm.html?keyconfirm="
								+ AdvancedEncryptionStandard.encrypt(user.getEmail())
								+ "'>Reset Your Password</a><br><br>Regards,<br>Team Engage.");
				data.put("status", true);
				// restTemplate.postForObject("http://35.166.195.23:8080/EmailMicroservice/email/send",
				// data,String.class );
				userService.sendEmail(data);
				// restTemplate.postForObject(emailMicroserviceURL +
				// "/email/send", data, String.class);
				// restTemplate.postForObject("http://localhost:8080/email/send",
				// data,String.class );

				return response;
			} else {
				response.setMessage("Email not registered.");
				response.setStatuscode(204);
				return response;
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatuscode(204);
			return response;
		}
	}

	@PreAuthorize("#oauth2.hasScope('client_app') and hasAnyAuthority('A','U')")
	@RequestMapping(value = "/api/v1/userbasicinfo", method = RequestMethod.GET)

	public @ResponseBody JsonMessage getUserInfo(HttpServletRequest request) {
		JsonMessage response = new JsonMessage();
		try {
			if (request.getUserPrincipal() != null) {
				String userEmail = request.getUserPrincipal().getName();

				// User validateUser = _userDao.getById(userEmail);
				User validateUser = userService.getUserByEmail(userEmail);
				Map<String, Object> data1 = new HashMap<String, Object>();
				data1.put("UserBacsicInfo", validateUser);
				response.setMessage("User info");
				response.setData(data1);
				response.setStatuscode(200);
				return response;
			} else {
				throw new AuthenticationCredentialsNotFoundException("You are not authorized to access this resource");
			}
		} catch (AuthenticationCredentialsNotFoundException aex) {
			response.setData(aex.getMessage());
			response.setStatuscode(403);
			return response;

		} catch (Exception ex) {

			response.setMessage(ex.getMessage());

			response.setStatuscode(204);
			return response;
		}
	}

}