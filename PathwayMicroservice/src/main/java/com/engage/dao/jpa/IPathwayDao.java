/**
 * 
 */
package com.engage.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engage.dto.PathwayAndEventNames;
import com.engage.model.Pathway;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPathwayDao extends JpaRepository<Pathway, Long> {

	@Query(nativeQuery = true, value = "select p.id as pathwayId, p.Pathway_name as pathwayName,e.id as eventId,e.event_name as eventName,ppi.patient_id as patientId,ppi.accepteddate as acceptedDate, p.org_id as orgId from qc_pathway p inner join qc_pathway_event e on p.id=e.pathway_id left join qc_patient_pathway_info ppi on p.id=ppi.pathway_id where p.org_id=?1")
	public List<Object[]> getPathwayEventsAcceptedDate(Long orgId);
}
