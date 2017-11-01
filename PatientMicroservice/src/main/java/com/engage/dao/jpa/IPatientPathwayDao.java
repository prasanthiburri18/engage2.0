/**
 * 
 */
package com.engage.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.PatientPathway;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPatientPathwayDao extends JpaRepository<PatientPathway, Long> {
	
	List<PatientPathway> findByPatientid(Long patientid);

}
