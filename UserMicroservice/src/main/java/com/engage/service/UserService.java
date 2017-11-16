package com.engage.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.engage.commons.dto.PatientDto;
import com.engage.commons.exception.DataTamperingException;
import com.engage.commons.exception.UserNotFoundException;
import com.engage.dao.OrganizationDao;
import com.engage.dao.UserDao;
import com.engage.dao.jpa.IOrganizationDao;
import com.engage.dao.jpa.IUserDao;
import com.engage.dao.jpa.IUserRolesDao;
import com.engage.model.Organization;
import com.engage.model.ScheduleJson;
import com.engage.model.User;
import com.engage.model.UserRoles;
import com.engage.util.AdvancedEncryptionStandard;
import com.engage.util.JsonMessage;

@Service
@Transactional
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao _userDao;
	@Autowired
	private IUserRolesDao userRolesDaoJpa;
	
	@Autowired
	private IOrganizationDao organizationDaoJpa;
	@Autowired
	private IUserDao userDaoJpa;
	@Autowired
	private OrganizationDao _organizationDao;

	@Value("${emailMicroService.URL}")
	public String emailMicroserviceURL;
	@Value("${portal.URL}")
	public String portalURL;
	@Value("${patientMicroserviceUrl}")
	public String patientMicroserviceUrl;

	@Value("${pathwayMicroserviceUrl}")
	private String pathwayMicroserviceUrl;
	@Autowired
	private OAuth2RestTemplate restTemplate;

	/**
	 * For encrypting password. Removed previous encoder.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void sendEmail(Map<String, Object> data) {
		final String simpleMailUrl = emailMicroserviceURL + "/email/send";
		restTemplate.postForObject(simpleMailUrl, data, String.class);
	}

	public User getUserByEmail(String email) throws UserNotFoundException {
		User user = userDaoJpa.findUserByEmailIgnoreCase(email);
		if (user == null) {
			throw new UserNotFoundException("Given email id doesn't exists");
		}
		return user;
	}
	
	@Transactional
	public void register(User user) throws Exception {

		if (user.getId() != null ) {
			throw new DataTamperingException("Before Registering UserId should not be assigned");
		}
		LOGGER.info("Saving Organization..");
		Organization org = new Organization();
		org.setName(user.getPracticeName());

		Integer orgid = _organizationDao.save(org);
		LOGGER.info(" Organization saved. OrgId = " +orgid);
		user.setOrgid(orgid);
		LOGGER.info("Check if password is null "+ user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		LOGGER.info("Encoded password ");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		LOGGER.info("Got time stam");
		user.setCreateDate(timestamp);
		user.setUpdateDate(timestamp);
		LOGGER.info("Registering new user");
		BigInteger id = _userDao.save(user);

		UserRoles userRoles = new UserRoles();
		userRoles.setUserId(id);
		userRoles.setRoleId(1);
		LOGGER.info("Before saving Roles");
		userRolesDaoJpa.save(userRoles);
		
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

		sendEmail(data1);
		
	}

	public ArrayList<Object> patientidbyphone(final String phone) {
		try {
			String encryptPhone = AdvancedEncryptionStandard.encrypt(phone);
			encryptPhone = encryptPhone.trim();

			List<Object[]> results = restTemplate
					.getForObject(patientMicroserviceUrl + "/api/v1/patient/phone/" + encryptPhone, List.class);
			ArrayList<Object> blockres = new ArrayList<>();
			Object[] obj = new Object[] {};
			ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
			for (Object[] row : results) {
				Map<String, String> assoc = new HashMap<String, String>();

				String rid = row[0].toString();

				String cp = row[6].toString();
				if (AdvancedEncryptionStandard.decrypt(cp).equals(phone)) {
					blockres.add(rid);
				}

			}

			return blockres;
		} catch (Exception ex) {
			return null;
		}

	}

	public int verifyPatientInfobydob(String pdob) {
		List<Object> patientList = restTemplate.getForObject(patientMicroserviceUrl + "/api/v1/patient/dob/" + pdob,
				List.class);
		int size = 0;
		if (patientList != null) {
			size = patientList.size();
		}
		return size;

	}
	
	/**
	 * Get patients list by Date of Birth
	 * 
	 * @param pdob
	 * @return
	 */
	public List<PatientDto> getPatientsListByDob(String pdob){
		ResponseEntity<List<PatientDto>> patientList = restTemplate.exchange(patientMicroserviceUrl + "/api/v1/patient/dob/" + pdob,HttpMethod.GET,null,
				new ParameterizedTypeReference<List<PatientDto>>() {
				});
		return patientList.getBody();
	}
	public int verifyPatientInfo(long patientid) {

		List<Object> patientPathwayInfoList = restTemplate
				.getForObject(pathwayMicroserviceUrl + "/api/v1/pathway/patient?id=" + patientid, List.class);
		int statuscode = 0;
		if (patientPathwayInfoList != null) {
			statuscode = patientPathwayInfoList.size();
		}
		return statuscode;
	}

	public JsonMessage getPatientpathwayblockById(int id) {
		JsonMessage response = null;
		Map<String, String> json = new HashMap<>();
		json.put("id", Integer.toString(id));
		LOGGER.info("Calling Pathway Microservice with phi block id");
		response = restTemplate
				.postForObject(pathwayMicroserviceUrl + "/api/v1/getPatientpathwayblockbyId", json, JsonMessage.class);
		
		return response;
/*		List<Object> scheduledData = new ArrayList<Object>();
		try {

			Map<String, String> json = new HashMap<>();
			json.put("id", Integer.toString(id));
			LOGGER.info("Calling Pathway Microservice with phi block id");
			List<Object[]> results = restTemplate
					.postForObject(pathwayMicroserviceUrl + "/api/v1/getPatientpathwayblockbyId", json, List.class);
			response = restTemplate
					.postForObject(pathwayMicroserviceUrl + "/api/v1/getPatientpathwayblockbyId", json, JsonMessage.class);
			
			LOGGER.info("Pathway Microserivce request executed.");
			ArrayList blockres = new ArrayList();
			Object[] obj = new Object[] {};
			ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
			for (Object[] row : results) {
				LOGGER.info("Converting object array to ScheduleJson object");
				ScheduleJson scheduleJson = new ScheduleJson();
				Map<String, Object> assoc = new HashMap<String, Object>();
				Integer rid = Integer.parseInt(row[0].toString());
				assoc.put("id", rid);
				Integer pthwayid = Integer.parseInt(row[1].toString());
				assoc.put("pathway_id", pthwayid);
				Integer ppatientid = Integer.parseInt(row[2].toString());
				assoc.put("patient_id", ppatientid);
				Integer bblocid = Integer.parseInt(row[3].toString());
				assoc.put("block_id", bblocid);
				String bappointmentdate = row[4].toString();
				assoc.put("block_appointment_date", bappointmentdate);
				Integer bparentid = Integer.parseInt(row[5].toString());
				assoc.put("block_parent_id", bparentid);
				String blockname = row[6].toString();
				assoc.put("block_name", blockname);
				String blocktype = row[7].toString();
				assoc.put("block_type", blocktype);
				Integer blockposrow = Integer.parseInt(row[8].toString());
				assoc.put("block_pos_row", blockposrow);
				String messagesendat = row[9].toString();
				assoc.put("message_send_at", messagesendat);
				String messagestatus = row[10].toString();
				assoc.put("message_status", messagestatus);
				String patient_accepted_date = row[11].toString();
				assoc.put("patient_accepted_date", patient_accepted_date);
				Integer block_pos_col = Integer.parseInt(row[12].toString());
				assoc.put("block_pos_col", block_pos_col);
				Integer trigger_id = Integer.parseInt(row[13].toString());
				assoc.put("trigger_id", trigger_id);
				Integer delivery_days_after_trigger = Integer.parseInt(row[14].toString());
				assoc.put("delivery_days_after_trigger", delivery_days_after_trigger);
				Integer repeat_for_number_of_days = Integer.parseInt(row[15].toString());
				assoc.put("repeat_for_number_of_days", repeat_for_number_of_days);
				String subject_of_message = row[16].toString();
				assoc.put("subject_of_message", subject_of_message);
				String body_of_message = row[17].toString();
				assoc.put("body_of_message", body_of_message);
				String remindermessage = row[18].toString();
				assoc.put("remainder_of_message", remindermessage);
				String fmessage = row[19].toString();
				assoc.put("followup_of_message", fmessage);
				String status = row[20].toString();
				assoc.put("status", status);
				String created_date = row[21].toString();
				assoc.put("created_date", created_date);
				Integer event_id = Integer.parseInt(row[22].toString());
				assoc.put("event_id", event_id);
				blockres.add(assoc);

			}

			return blockres;

		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());

			return scheduledData;
		}*/

	}
	
	public List<User> getUsersByOrgId(Integer orid) {
		List<User> users = null;
		users = userDaoJpa.getUserByOrgid(orid);
		return users;
	}

}
