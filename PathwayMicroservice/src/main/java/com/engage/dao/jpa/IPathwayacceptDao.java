/**
 * 
 */
package com.engage.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Pathwayaccept;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPathwayacceptDao extends JpaRepository<Pathwayaccept, Long> {

	public List<Pathwayaccept> findByPatientId(long patientId);
}
