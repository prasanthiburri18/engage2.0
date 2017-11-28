/**
 * 
 */
package com.engage.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

import com.engage.commons.dto.PathwayDto;
import com.engage.commons.dto.PathwayListDto;
import com.engage.commons.dto.PathwayPatientCountDto;
import com.engage.commons.dto.PatientDto;
import com.engage.commons.dto.PatientListDto;
import com.engage.commons.exception.PathwayNotFoundException;
import com.engage.dao.BlocksDao;
import com.engage.dao.PathwayDao;
import com.engage.dao.jpa.IPathwayDao;
import com.engage.dao.jpa.IPathwayacceptDao;
import com.engage.dto.PathwayAndEventNames;
import com.engage.dto.PathwayPatientBlockDto;
import com.engage.exception.PatientNotAcceptedException;
import com.engage.exception.PatientPathwayBlockNotFoundException;
import com.engage.model.Pathway;
import com.engage.model.Pathwayaccept;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mindtech-labs
 *
 */
@Service
@Transactional
public class PathwayService {

	
	private static final Logger logger = LoggerFactory.getLogger(PathwayService.class);
	@Autowired
	private IPathwayacceptDao pathwayAcceptDao;

	@Autowired
	private PathwayDao _pathwayDao;
	
	@Autowired
	private BlocksDao _blocksDao;
	@Autowired
	private IPathwayDao pathwayDao;
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private OAuth2RestTemplate restTemplate;
	
	@Value("${patientMicroserviceUrl}")
	private String patientMicroserviceUrl;
	public List<Pathwayaccept> findIfPatientAcceptedPathway(long patientId) throws PatientNotAcceptedException{
		List<Pathwayaccept> patientAcceptList = pathwayAcceptDao.findByPatientId(patientId);
		if(patientAcceptList==null||patientAcceptList.size()<1){
			throw new PatientNotAcceptedException("Patient didn't accept pathway yet");
		}
		return patientAcceptList;
	}
	/**
	 * Used for getting phi blocks
	 * @param rid
	 * @return
	 * @throws PatientPathwayBlockNotFoundException 
	 */
	public List<PathwayPatientBlockDto> 	getPatientpathwayblockById(int rid) throws PatientPathwayBlockNotFoundException{
		List<PathwayPatientBlockDto> blockDtos = null;
		
		List<Object> results = _blocksDao.getPatientpathwayblockById(rid);
		
		if(results!=null&&results.size()>0){
			blockDtos = new ArrayList<>();
			
			ObjectMapper mapper = new ObjectMapper();
			//results.stream().forEach(ob->blockDtos.add(mapper.convertValue(ob,BlocksDto.class)));
			
			for(Object ob:results){
			
				blockDtos.add(mapper.convertValue(ob,PathwayPatientBlockDto.class));
			}
			
			logger.info("Size of Blocks "+(blockDtos==null?0:blockDtos.size()));
			
		}
		else{
			throw new PatientPathwayBlockNotFoundException("No block with given id exists");
		}
		
		return blockDtos;
	}
	
	public List<PathwayAndEventNames> getPathwayEventNamesAndAcceptedStatus(Long orgId) {
		logger.info("Getting pathway, event names and accepted status");
		List<PathwayAndEventNames> list = null;
		List<Object[]> objects = pathwayDao.getPathwayEventsAcceptedDate(orgId);
		if(!objects.isEmpty()){
			list = new ArrayList<>();
			for(Object[] object:objects){
				PathwayAndEventNames pathwayAndEventNames = convertObjectToPathwayEventsDto(object);
				list.add(pathwayAndEventNames);
				
			}
			
		}
		logger.info("Size of list of Pathway, Event Names, Accepted status "+objects.size());
		return list;
	}
	
	/**
	 * "select p.id as pathwayId, p.Pathway_name as pathwayName,e.id as eventId,e.event_name as eventName,
	 * ppi.patient_id as patientId,ppi.accepteddate as acceptedDate, p.org_id as orgId from qc_pathway p inner join q
	 * @param object
	 * @return
	 */
	private PathwayAndEventNames convertObjectToPathwayEventsDto(Object[] object) {
		PathwayAndEventNames pen = null;
		if(object!=null){
			int i=0;
			pen = new PathwayAndEventNames();
			pen.setPathwayId((Integer) object[i]);
			pen.setPathwayName((String) object[++i]);
			pen.setEventId((Integer) object[++i]);
			pen.setEventName((String)object[++i]);
			pen.setPatientId((Integer)object[++i]);
			pen.setAcceptedDate((Date) object[++i]);
			pen.setOrgId((Integer)object[++i]);
		}
		return pen;
		
	}
	public int updatePatientPathwayMessageStatus(Integer messageId) {
	int status=	_pathwayDao.updatePatientmessagestatus(messageId);
		
	return status;
	}
	public List<Object> getBlockcrondataexecute() {
		
		return _blocksDao.getBlockcrondataexecute();
		
	}
	
public List<Object> getFirstdayBlockcrondataexecute() {
		
		return _blocksDao.getFirstdayBlockcrondataexecute();
		
	}
public List<Object> getPathwayFirstMessageforpatient(int pathwayId) {
	
	return _blocksDao.getPathwayFirstMessage(pathwayId);
}
public List<PathwayListDto> getPathwayList(Long orgId) throws PathwayNotFoundException {
	List<Pathway> pathways = _pathwayDao.getAll(orgId);
	List<PathwayListDto> pathwayListDto = null;
	List<PathwayDto> pathwayDtos=null;
	if(pathways!=null && !pathways.isEmpty()){
		pathwayDtos = new ArrayList<>();
		
		// pathways.forEach(p->pathwayDtos.add(objectMapper.convertValue(p, PathwayDto.class)));
		for(Pathway p: pathways){
			pathwayDtos.add(objectMapper.convertValue(p, PathwayDto.class));
		}
		 logger.info(" Size of list "+pathwayDtos);
			pathwayListDto = new ArrayList<>();
			for(PathwayDto p : pathwayDtos){
				PathwayListDto pld = new PathwayListDto();
				pld.setPathwayDto(p);
				pathwayListDto.add(pld);
			}
	}
	else{
		throw new PathwayNotFoundException("No pathways found against given organization id:"+orgId);
	}
	if(pathwayListDto!=null){
	final String patientListUrl =patientMicroserviceUrl+"/api/v1/pathway/patients?orgId="+orgId; 
	ResponseEntity<List<PathwayPatientCountDto>> patientListResponse = restTemplate.exchange(patientListUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<PathwayPatientCountDto>>() {
	});
	List<PathwayPatientCountDto> patientDtoList = patientListResponse.getBody();

	logger.info("Size of patientDtoList "+(patientDtoList==null?"null":patientDtoList.size()));

	if(patientDtoList!=null&&!patientDtoList.isEmpty()){
		logger.info("List of patients assigned with pathways is not empty. Size: "+patientDtoList.size());
		for(PathwayListDto pld :pathwayListDto){
			Long pathwayId = pld.getPathwayDto().getId();
			Long count = new Long(0);
			for(PathwayPatientCountDto ppcd : patientDtoList){
				if(ppcd.getPathwayId().longValue()==pathwayId.longValue()){
					logger.info("Assigning pathway count to pathwayDto");
					count = ppcd.getPathwayCount().longValue();
				}
			}
			
			pld.setPatientsCount(count);
			
		}
	}
	}
	return pathwayListDto;
}
	

/*private PathwayDto convertPathwayModelToDto(Pathway pathway){
	
	return 
}
private EventsDto convertEventsModeltoDto(Events events){
	
}
private BlocksDto convertBlocksModelToDto(Blocks blocks){
	
}*/
}
