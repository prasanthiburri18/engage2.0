/**
 * 
 */
package com.engage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.engage.dao.jpa.IPatientDao;
import com.engage.dao.jpa.IPatientPathwayDao;
import com.engage.dto.PathwayAndEventNames;
import com.engage.dto.PatientDto;
import com.engage.dto.PatientListDto;
import com.engage.dto.WrapperPathwayAndEventNames;
import com.engage.exception.InvalidDateOfBirthException;
import com.engage.exception.PatientNotFoundException;
import com.engage.model.Patient;
import com.engage.model.PatientPathway;
import com.engage.util.PatientDtoToModelUtils;

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

	@Autowired
	private IPatientPathwayDao patientPathwayDaoJpa;

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
public PatientDto getPatientById(long id) throws PatientNotFoundException{
	
	PatientDto patientDto = null;
	Patient patient = null;
	patient = patientDaoJpa.findOne(id);
	
	if(patient!=null){
		patientDto = PatientDtoToModelUtils.convertModelToDto(patient);
		return patientDto;
		
	}
	else{
		throw new PatientNotFoundException("No patient found");
	}
	
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
			LOGGER.warn("No patients found against given date of birth");
			throw new PatientNotFoundException("No patients found against given date of birth");
		}

		return patientDtoList;
	}

	public Map<Long, PatientListDto> getPatientList(Long orgId) throws PatientNotFoundException {

		List<PatientDto> patientDtoList = null;
		List<Patient> patientList = patientDaoJpa.findByOrgId(orgId);
		if (patientList != null && patientList.size() > 0) {
			patientDtoList = patientList.stream().map(p -> PatientDtoToModelUtils.convertModelToDto(p))
					.collect(Collectors.toList());
		} else {
			LOGGER.warn("No patients found against given Organization Id");
			throw new PatientNotFoundException("No patients found against given Organization Id");
		}
		List<PatientPathway> patientPathwayMapping = null;
		LOGGER.info("Is patientdto list empty: " + patientDtoList.isEmpty());
		if (patientDtoList != null && !patientDtoList.isEmpty()) {
			patientPathwayMapping = new ArrayList<>();
			for (PatientDto pDto : patientDtoList) {

				List<PatientPathway> patientPathwayList = patientPathwayDaoJpa.findByPatientid(pDto.getId());
				if (patientPathwayList != null) {
					patientPathwayMapping.addAll(patientPathwayList);
				}
			}

		}
		Map<Long, PatientListDto> patientListDtos = new LinkedHashMap<>();

		// Adds patientDto objects to the map of patientListDto
		patientDtoList.forEach(p -> patientListDtos.put(p.getId(), new PatientListDto(p)));
		
		
		for (PatientPathway pp : patientPathwayMapping) {
			PatientListDto pld = patientListDtos.get(pp.getPatientid());
			pld.getEventIds().add(pp.getEventId());
			pld.setPathwayId((int) pp.getPathwayId());
		}
		
		for(PatientListDto pld : patientListDtos.values()){
			
			if(!(pld.getPathwayId()>0)){
				LOGGER.warn("No pathway for "+ pld.getId());
			}
		}

		if (patientPathwayMapping != null && !patientPathwayMapping.isEmpty()) {

			WrapperPathwayAndEventNames wrapper = restTemplate.getForObject(
					pathwayMicroserviceBaseUrl + "/api/v1/patientpathwaylist?orgId=" + orgId,
					WrapperPathwayAndEventNames.class);
			List<PathwayAndEventNames> listPathwayEventNames = wrapper.getList();
			// Write logic for converting listPathwayAndEventNames

			Map<Long, String> eventNamesOfOrganization = new HashMap<>();
			Map<Long, String> pathwayNamesOfOrganization = new HashMap<>();
			Map<Long, Date> patientAcceptedOfOrganization = new HashMap<>();
			for (PathwayAndEventNames paen : listPathwayEventNames) {
				eventNamesOfOrganization.put(new Long(paen.getEventId()), paen.getEventName());
			}
			
			for (PathwayAndEventNames paen : listPathwayEventNames) {
				pathwayNamesOfOrganization.put(new Long(paen.getPathwayId()), paen.getPathwayName());
			}
			
			for (PathwayAndEventNames paen : listPathwayEventNames) {
				if(paen.getPathwayId()!=null &&paen.getPatientId()!=null&&paen.getAcceptedDate()!=null){
					
				patientAcceptedOfOrganization.put(new Long(paen.getPatientId()),paen.getAcceptedDate());
				}
				}
	
			for (PatientListDto pld : patientListDtos.values()) {
			//	pld.getEventNames().addAll(pld.getEventIds().stream().map(eid -> eventNamesOfOrganization.get(eid))
			//			.collect(Collectors.toSet()));
				Set<String> eventNames = new LinkedHashSet<>();
				if(pld.getEventIds().size()==0){
					LOGGER.warn("No events in this pathway."+pld.getPathwayId()+" "+"patientId:"+pld.getId());
				}

				for(Long eventId:pld.getEventIds()){
					
					LOGGER.info("Event Name "+eventNamesOfOrganization.get(eventId));
					LOGGER.info(eventNamesOfOrganization.keySet().toString());
					LOGGER.info("Event Id "+eventId+" key exists "+eventNamesOfOrganization.keySet().contains(eventId));
					LOGGER.info("Event Id "+eventId+" key exists "+eventNamesOfOrganization.containsKey(eventId));
					eventNames.add(eventNamesOfOrganization.get(eventId));
				}
				pld.getEventNames().addAll(eventNames);
				
				String pathwayName = pathwayNamesOfOrganization.get(new Long(pld.getPathwayId()));
				pathwayName = (pathwayName==null||pathwayName.equals(""))?"-":pathwayName;
				pld.setPathwayName(pathwayName);
				Date acceptedDate = patientAcceptedOfOrganization.get(new Long(pld.getId()));
				if(acceptedDate!=null){
					pld.setAccepted(true);
					pld.setDaysSinceStart(TimeUnit.DAYS.convert((new Date().getTime()
							  - acceptedDate.getTime()), TimeUnit.MILLISECONDS));
				}
				else{
					pld.setAccepted(false);
					pld.setDaysSinceStart((long)0);
				}
			}
			/*
			 * for (PathwayAndEventNames paen : listPathwayEventNames) {
			 * 
			 * PatientListDto pld = patientListDtos.get(paen.getPatientId());
			 * //This is checked because patient might be deleted but his
			 * pathway details are not. if(pld!=null){
			 * if(pld.getEventIds().contains(paen.getEventId())){
			 * pld.getEventNames().add(paen.getEventName()); }
			 * if(pld.getPathwayId()==paen.getPathwayId()){
			 * pld.setPathwayName(paen.getPathwayName()); }
			 * pld.setAccepted(paen.getAcceptedDate()!= null ? true : false);
			 * 
			 * 
			 * }
			 */
		}return patientListDtos;
}
	public ArrayList getPathwayFirstMessageforpatient(int pathwayId) {
		ResponseEntity<List<Object>> firstMessage = (ResponseEntity<List<Object>>) restTemplate.exchange(
				pathwayMicroserviceBaseUrl + "/api/v1/pathway/welcomemessage?pathwayId="+pathwayId, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Object>>() {
				});
		
		return (ArrayList) firstMessage.getBody();
	}}
