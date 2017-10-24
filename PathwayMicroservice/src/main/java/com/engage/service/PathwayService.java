/**
 * 
 */
package com.engage.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engage.dao.jpa.IPathwayacceptDao;
import com.engage.exception.PatientNotAcceptedException;
import com.engage.model.Pathwayaccept;

/**
 * @author mindtech-labs
 *
 */
@Service
@Transactional
public class PathwayService {

	@Autowired
	private IPathwayacceptDao pathwayAcceptDao;
	public List<Pathwayaccept> findIfPatientAcceptedPathway(long patientId) throws PatientNotAcceptedException{
		List<Pathwayaccept> patientAcceptList = pathwayAcceptDao.findByPatientId(patientId);
		if(patientAcceptList==null||patientAcceptList.size()<1){
			throw new PatientNotAcceptedException("Patient didn't accept pathway yet");
		}
		return patientAcceptList;
	}
	
}
