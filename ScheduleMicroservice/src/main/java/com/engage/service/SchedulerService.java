/**
 * 
 */
package com.engage.service;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.engage.commons.dto.PatientDto;
import com.engage.commons.exception.MessageUpdateFailedException;
import com.engage.commons.exception.PatientNotFoundException;

/**
 * @author mindtechlabs
 *
 */
@Service
public class SchedulerService {

	@Autowired
	private OAuth2RestTemplate restTemplate;

	@Value("${patientMicroserviceUrl}")
	private String patientMicroserviceUrl;

	@Value("${pathwayMicroserviceUrl}")
	private String pathwayMicroserviceUrl;

	public String patientPhoneById(final Integer patientId) {

		try {

			ResponseEntity<PatientDto> patientDtoResponse = (ResponseEntity<PatientDto>) restTemplate.exchange(
					patientMicroserviceUrl + "/api/v1/patient?id=" + patientId, HttpMethod.GET, null,
					new ParameterizedTypeReference<PatientDto>() {
					});
			if (patientDtoResponse.getStatusCodeValue() == 200) {
				return patientDtoResponse.getBody().getPhone();
			} else {
				throw new PatientNotFoundException("No patient found against given id");
			}

		} catch (Exception ex) {
			return null;
		}

	}

	public int updatePatientmessagestatus(int id) throws MessageUpdateFailedException {

		ResponseEntity<Object> updateResponse = (ResponseEntity<Object>) restTemplate.exchange(
				pathwayMicroserviceUrl + "/api/v1/patientpathwayblock/message?id=" + id, HttpMethod.PUT, null,
				new ParameterizedTypeReference<Object>() {
				});
		if (updateResponse.getStatusCodeValue() == 202) {
			return updateResponse.getStatusCodeValue();
		} else {
			throw new MessageUpdateFailedException("Cannot update status of given message " + id);
		}

	}

	/**
	 * This is always a list(empty or full). Won't return null
	 * @return
	 */
	public List<Object> getBlockcrondataexecute() {

		ResponseEntity<List<Object>> cronMessages = (ResponseEntity<List<Object>>) restTemplate.exchange(
				pathwayMicroserviceUrl + "/api/v1/scheduledmessages", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Object>>() {
				});
		
		return cronMessages.getBody();
	}

	public List<Object> getFirstdayBlockcrondataexecute() {
		ResponseEntity<List<Object>> cronMessages = (ResponseEntity<List<Object>>) restTemplate.exchange(
				pathwayMicroserviceUrl + "/api/v1/firstdaymessages", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Object>>() {
				});
		
		return cronMessages.getBody();
	}

}
