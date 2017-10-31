/**
 * 
 */
package com.engage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.engage.dao.jpa.IPatientDao;
import com.engage.dto.PathwayAndEventNames;
import com.engage.dto.PathwayDto;
import com.engage.dto.PatientDto;
import com.engage.dto.PatientListDto;
import com.engage.dto.WrapperPathwayAndEventNames;
import com.engage.exception.InvalidDateOfBirthException;
import com.engage.exception.PatientNotFoundException;
import com.engage.model.Patient;
import com.engage.util.PatientDtoToModelUtils;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author mindtech-labs
 *
 */
@Service
@Transactional
public class PatientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);
	@Autowired
	private IPatientDao patientDaoJpa;

	@Value("${pathwayMicroserviceBaseUrl}")
	private String pathwayMicroserviceBaseUrl;
	@Autowired
	private OAuth2RestTemplate restTemplate;

	public List<PatientDto> getPatientsByPhoneNumber(String phone) throws PatientNotFoundException {
		List<PatientDto> patientDtoList = null;
		List<Patient> patientList = patientDaoJpa.findPatientByPhone(phone);
		if (patientList != null && patientList.size() > 0) {
			patientDtoList = patientList.stream().map(p -> PatientDtoToModelUtils.convertModelToDto(p))
					.collect(Collectors.toList());
		} else {
			LOGGER.warn("No patients found against given phone number");
			throw new PatientNotFoundException("No patients found against the given phone number");
		}
		return patientDtoList;
	}

	public List<PatientDto> getPatientByDob(String dob) throws InvalidDateOfBirthException, PatientNotFoundException {
		List<PatientDto> patientDtoList = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date utilDate;
		try {
			utilDate = dateFormat.parse(dob);
		} catch (ParseException e) {
			throw new InvalidDateOfBirthException("Date of birth format passed in invalid. " + e.getMessage());
		}
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

		List<Patient> patientList = patientDaoJpa.findPatientByDob(sqlDate);
		if (patientList != null && patientList.size() > 0) {
			patientDtoList = patientList.stream().map(p -> PatientDtoToModelUtils.convertModelToDto(p))
					.collect(Collectors.toList());
		} else {
			LOGGER.warn("No patients found against given phone number");
			throw new PatientNotFoundException("No patients found against the given phone number");
		}

		return patientDtoList;
	}

	public List<PatientListDto> getPatientList(Long orgId)
			throws PatientNotFoundException {

		List<PatientListDto> patientListDtos = null;
		List<PatientDto> patientDtoList = null;
		List<Patient> patientList = patientDaoJpa.findByOrgId(orgId);
		if (patientList != null && patientList.size() > 0) {
			patientDtoList = patientList.stream()
					.map(p -> PatientDtoToModelUtils.convertModelToDto(p))
					.collect(Collectors.toList());
		} else {
			LOGGER.warn("No patients found against given Organization Id");
			throw new PatientNotFoundException(
					"No patients found against given Organization Id");
		}
		patientListDtos = new ArrayList<PatientListDto>();
		
		
		WrapperPathwayAndEventNames wrapper  =restTemplate.getForObject(
				pathwayMicroserviceBaseUrl
						+ "/api/v1/patientpathwaylist?orgId=" +orgId ,WrapperPathwayAndEventNames.class
						);
		List<PathwayAndEventNames> listPathwayEventNames = wrapper.getList();
		//Write logic for converting listPathwayAndEventNames
		
		return patientListDtos;
	}
}
