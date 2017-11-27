/**
 * 
 */
package com.engage.dao.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engage.model.PatientPathway;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPatientPathwayDao extends JpaRepository<PatientPathway, Long> {

	List<PatientPathway> findByPatientid(Long patientid);

	@Query(nativeQuery = true, value = "select distinct pathway_id as pathwayId, count(distinct patient_id) as patientCount from qc_patient_pathway where patient_id in (select id from qc_patient p where p.org_id=?1) group by pathway_id;")
	List<Object[]>getPathwayCount(Long orgId);

}
