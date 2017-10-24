package com.engage.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.engage.commons.exception.DataTamperingException;
import com.engage.dao.OrganizationDao;
import com.engage.dao.UserDao;
import com.engage.model.Organization;
import com.engage.model.User;
import com.engage.util.AdvancedEncryptionStandard;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao _userDao;

	@Autowired
	private OrganizationDao _organizationDao;

	@Value("${emailMicroService.URL}")
	public String emailMicroserviceURL;
	@Value("${portal.URL}")
	public String portalURL;

	@Autowired
	private OAuth2RestTemplate restTemplate;
	
	/**
	 * For encrypting password. Removed previous encoder.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean register(User user) throws Exception {
		boolean registerFlag = false;
		if (user.getId() != null || user.getId().intValue() <= 0) {
			throw new DataTamperingException("Before Registering UserId should not be assigned");
		}
		

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

		
		final String simpleMailUrl = emailMicroserviceURL + "/email/send";
		restTemplate.postForObject(simpleMailUrl, data1, String.class);
		
		Organization org = new Organization();
		org.setName(user.getPracticeName());

		Integer orgid = _organizationDao.save(org);
		user.setOrgid(orgid);
	
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		user.setCreateDate(timestamp);
		user.setUpdateDate(timestamp);

		
		BigInteger id = _userDao.save(user);

		
		return registerFlag;
	}

}
