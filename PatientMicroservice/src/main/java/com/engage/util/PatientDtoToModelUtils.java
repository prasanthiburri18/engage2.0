/**
 * 
 */
package com.engage.util;

import com.engage.dto.PatientDto;
import com.engage.model.Patient;

/**
 * @author ai
 *
 */
public class PatientDtoToModelUtils {

	public synchronized static Patient convertDtoToModel(final PatientDto patientDto) {
		Patient patient = null;
		// getter of orgId has lower 'o' from engage1.0
		if (patientDto != null) {
			patient = new Patient();
			patient.setEmail(patientDto.getEmail());
			patient.setFirstName(patientDto.getFirstName());
			patient.setLastName(patientDto.getLastName());
			patient.setPhone(patientDto.getPhone());
			patient.setDeviceToken(patientDto.getDeviceToken());
			patient.setStatus(patientDto.getStatus());
			patient.setCreateDate(patientDto.getCreateDate());
			patient.setUpdateDate(patientDto.getUpdateDate());
			patient.setDob(patientDto.getDob());
			patient.setClinicianId(patientDto.getClinicianId());
			patient.setorgId(patientDto.getorgId());

			patient.setEvents(patientDto.getEvents());
			// To tackle edit issue
			if (patientDto.getId() != 0) {
				patient.setId(patientDto.getId());
			}
			patient.setPathwayId(patientDto.getPathwayId());
			patient.setPathwayName(patientDto.getPathwayName());
		}
		return patient;
	}

	
	public synchronized static PatientDto convertModelToDto(final Patient patient) {
		PatientDto patientDto = null;
		// getter of orgId has lower 'o' from engage1.0
		if (patient != null) {
			patientDto= new PatientDto();
			patientDto.setEmail(patient.getEmail());
			patientDto.setFirstName(patient.getFirstName());
			patientDto.setLastName(patient.getLastName());
			patientDto.setPhone(patient.getPhone());
			patientDto.setDeviceToken(patient.getDeviceToken());
			patientDto.setStatus(patient.getStatus());
			patientDto.setCreateDate(patient.getCreateDate());
			patientDto.setUpdateDate(patient.getUpdateDate());
			patientDto.setDob(patient.getDob());
			patientDto.setClinicianId(patient.getClinicianId());
			patientDto.setorgId(patient.getorgId());

			patientDto.setEvents(patient.getEvents());
			// To tackle edit issue
			if (patient.getId() != 0) {
				patientDto.setId(patient.getId());
			}
			patientDto.setPathwayId(patient.getPathwayId());
			patientDto.setPathwayName(patient.getPathwayName());
		}
		return patientDto;
	}
}
