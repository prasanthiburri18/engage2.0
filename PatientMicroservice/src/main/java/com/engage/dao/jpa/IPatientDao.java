/**
 * 
 */
package com.engage.dao.jpa;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Patient;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPatientDao extends JpaRepository<Patient,Long>{

	public List<Patient> findPatientByPhone(String phone);
	
	
	public List<Patient> findPatientByDob(Date dob);
}
