/**
 * 
 */
package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.PatientPathwayMessages;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPatientPathwayMessagesDao extends JpaRepository<PatientPathwayMessages, Long> {

}
