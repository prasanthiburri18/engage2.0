/**
 * 
 */
package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.PatientPathway;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPatientPathwayDao extends JpaRepository<PatientPathway, Long> {

}
