package com.engage.controller;



import com.engage.dao.PathwayDao;
import com.engage.dao.BlocksDao;
import com.engage.dao.EventsDao;
import com.engage.model.Pathway;
import com.engage.model.ScheduleJson;

import com.engage.model.Events;
import com.engage.util.JsonMessage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
* Pathway MicroServices for Pathway API Operations
* 
* @author  StartUP Labs
* @version 1.0
* 
*/
@RestController
@RequestMapping(value="/api/v1")
public class PathwayController {
	  
	private static Logger log = LoggerFactory.getLogger(PathwayController.class);
  @Autowired
  private PathwayDao _pathwayDao;
  @Autowired
  private EventsDao _eventsDao;
  @Autowired
  private BlocksDao _blocksDao;

  /**
   * Add Pathway Method
   * 
   * @Inputparam pathway object
   * @return Jsonobject
   */

  @RequestMapping(value="/addPathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage addPathway(@RequestBody final Pathway pathway) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
		 Boolean isPathway= _pathwayDao.getByPathwayName(pathway.getPathwayName(),pathway.getorgId());
		 if(isPathway==true)
		 {
			 	response.setMessage("This pathway already exists");
				response.setStatuscode(208);
				return response;
		 }else
		 {
			 	pathway.setStatus("Y");
			 	
			 	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				 
			 	pathway.setCreateDate(timestamp);
			 	pathway.setUpdateDate(timestamp);
			 	
				Long id= _pathwayDao.save(pathway);
				response.setMessage("Pathway registered successfully");
				response.setData(id);
				response.setStatuscode(200);
				return response;
		 }
		
	  
	  }catch(Exception ex)
	 {
		  response.setMessage(ex.getMessage());
		  response.setStatuscode(204);
		  return response;
	 }
  }

  /**
   * Delete Pathway Method
   * 
   * @Inputparam Json object
   * @return Jsonobject
   */

  @RequestMapping(value="/deletePathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage deletePathway(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 Pathway pathway=_pathwayDao.getById(Long.parseLong(json.get("id")));
		 if(pathway.getId()>0)
		  {
			 _pathwayDao.delete(pathway);
			  response.setMessage("Pathway deleted successfully.");
			  response.setStatuscode(200);
			  return response;
		  }else
		  {
			  response.setMessage("Pathway not registered.");
			  response.setStatuscode(204);
			  return response;
		  }
	 }catch (Exception e) {
		 response.setMessage("Pathway not registered.");
		  response.setStatuscode(204);
		  return response;
	}
	 	 
  }
  
  /**
   * Delete Child Block Method
   * 
   * @Inputparam Json object
   * @return Jsonobject
   */
  @RequestMapping(value="/deletepatientchildblockbyid",method = RequestMethod.POST)
  public @ResponseBody JsonMessage deletepatientchildblockbyid(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 Integer deres=_blocksDao.deletePatientChildBlock(Long.parseLong(json.get("id")));
		 
			  response.setMessage("Block deleted successfully.");
			  response.setData(deres);
			  
			  response.setStatuscode(200);
			  return response;
		
	 }catch (Exception e) {
		 response.setMessage("Block Not Deleted.");
		  response.setStatuscode(204);
		  return response;
	}
	 	 
  }
  /**
   * Update Pathway Method
   * 
   * @Inputparam pathway object
   * @return JsonObject
   */
  @RequestMapping(value="/updatePathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage updatePathway(@RequestBody final Pathway pathway) 
  {
	  JsonMessage response=new JsonMessage();
		 try 
		  {
			 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			 
			 	
			 	pathway.setUpdateDate(timestamp);
			_pathwayDao.update(pathway);
			response.setMessage("Pathway updated successfully");
			response.setData(pathway);
			response.setStatuscode(200);
			return response;
		  
		  }catch(Exception ex)
		 {
			  response.setMessage("Pathway not registered");
			  response.setStatuscode(204);
			  return response;
		 }
	 	 
  }
  /**
   * List Pathway Method
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/listPathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listPathway(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try {
	    		List<Pathway> patient=_pathwayDao.getAll(Long.parseLong(json.get("orgId")));

	    		response.setMessage("Pathway list.");
	    		response.setData(patient);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }	 
  }
  /**
   * View Pathway Method
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/viewPathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage viewPathway(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try {
	    		List<Pathway> patient=_pathwayDao.verifyId(Long.parseLong(json.get("id")));
	    		response.setMessage("Pathway data.");
	    		response.setData(patient);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  /**
   * List Patient PathwayEvents Method
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/listPathwayEventForPatients",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listPathwayEventForPatients(@RequestBody Map<String,ArrayList<Long>> json) 
  {

	  JsonMessage response=new JsonMessage();
	    try {
	    	Map<String, Object> data=new HashMap<String, Object>();
	    	ArrayList<Long> pathwayid=json.get("id");
	    	List<Pathway> pathway=_pathwayDao.verifyId(pathwayid.get(0));
	    	ArrayList<Long> lst =  json.get("evetIds");
	    	List<Events> events=_eventsDao.getAllEvents(lst);
	    	Map<String, Object> pathwayinfo=new HashMap<String, Object>();
	    	pathwayinfo.put("id", pathway.get(0).getId());
	    	pathwayinfo.put("name", pathway.get(0).getPathwayName());
	    	pathwayinfo.put("updatedDate", pathway.get(0).getUpdateDate());
	    	data.put("pathwayInfo",pathwayinfo);
	    		data.put("eventInfo",events);
	    		response.setMessage("Pathway data.");
	    		response.setData(data);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  /**
   * Get Message Scheduled Records Method
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/getScheduledData",method = RequestMethod.POST)
  public @ResponseBody List<ScheduleJson> getScheduledData() 
  {
	  List<ScheduleJson> scheduledData=null;
	  JsonMessage response=new JsonMessage();
	    try {
	    	Map<String, Object> data=new HashMap<String, Object>();
	    	 scheduledData=_blocksDao.getScheduledData();
	    		response.setMessage("Scheduled data.");
	    		response.setData(scheduledData);
	    		response.setStatuscode(200);
	    		return scheduledData;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return scheduledData;
	    }
	 	 
  }
  
  /**
   * Loading Pathway SignUp Message Method
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/pathwayfirstMessage",method = RequestMethod.POST)
  public @ResponseBody  JsonMessage pathwayfirstMessage(@RequestBody Map<String, String> json) 
  {
	  List<Object> results=null;
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer pathwayid=Integer.parseInt(json.get("pathwayid").toString());
	    	Map<String, Object> data=new HashMap<String, Object>();
	    	results=_blocksDao.getPathwayFirstMessage(pathwayid);
	    		response.setMessage("Block Message Info.");
	    		response.setData(results);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  
  /**
   * Creating Patient Child Block
   * From Master Pathway assigned block
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/patientpathwaycron",method = RequestMethod.GET)
  public @ResponseBody JsonMessage patientPathwaycron() 
  {
	  JsonMessage response=new JsonMessage();
	    try {
	    	
	    		String blockcheck=_blocksDao.patientcron("run");
	    		response.setData(blockcheck);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  
  /**
   * Patient Pathway Method
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  
  @RequestMapping(value="/getPatientpathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getPatientpathway(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer ptid=Integer.parseInt(json.get("patientid").toString());
	    	Integer phtid=Integer.parseInt(json.get("pathwayid").toString());
	    	List resulst=_blocksDao.getPatientpathway(ptid,phtid);
	    		response.setMessage("Scheduled data.");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }

  /**
   * Loading Patient Pathway Information by Event
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/getPatientpathwayblockinfobyevent",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getPatientpathwayblockinfobyevent(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer ptid=Integer.parseInt(json.get("patientid").toString());
	    	Integer phtid=Integer.parseInt(json.get("pathwayid").toString());
	    	Integer eid=Integer.parseInt(json.get("eventid").toString());
	    	
	    	List resulst=_blocksDao.getPatientpathwayblocksByevent(ptid,phtid,eid);
	    		response.setMessage("Block  data.");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  /**
   * Loading Patient Pathway Information by Event
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/getPatientpathwayblockbyevent",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getPatientpathwayblockbyevent(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer ptid=Integer.parseInt(json.get("patientid").toString());
	    	Integer phtid=Integer.parseInt(json.get("pathwayid").toString());
	    	Integer eid=Integer.parseInt(json.get("eventid").toString());
	    	
	    	List resulst=_blocksDao.getPatientpathwayByevent(ptid,phtid,eid);
	    		response.setMessage("Block data.");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  
  /**
   * Loading Patient Pathway Information by Pathway
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/getPatientpathwayblockbyId",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getPatientpathwayblockbyId(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer rid=Integer.parseInt(json.get("id").toString());
	    	
	    	
	    	List resulst=_blocksDao.getPatientpathwayblockById(rid);
	    		response.setMessage("Scheduled data.");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  
  
  
  /**
   * Incase of Patient Pathway appointment.
   * We are creating new appointment block records by 
   * deleting old ones 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/createpatientpathwayblockrecord",method = RequestMethod.POST)
  public @ResponseBody JsonMessage createpatientpathwayblockrecord(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	
	    	
	    	
	    	Integer pwid=Integer.parseInt(json.get("pathwayId").toString());
	    	Integer patientid=Integer.parseInt(json.get("patientId").toString());			        			  
	    	Integer blockid=Integer.parseInt(json.get("blockid").toString());
	    	String appointmentdate=json.get("appointmentdate");
	    	Integer blockappointmentparent=Integer.parseInt(json.get("blockAppointmentParent").toString());
	    	String bname=json.get("blockName");
	    	String btype=json.get("blockType");
	    	Integer brow=Integer.parseInt(json.get("blockPocRow").toString());
	    	String messagesendat=json.get("messagesendat");
	    	String messeagesentstatus=json.get("messeagesentstatus");
	    	String patientaccepteddate=json.get("patientaccepteddate");
	    	String phisecured=json.get("phiSecured");
	    	Integer bcol=Integer.parseInt(json.get("blockPocCol").toString());
	    	Integer triggerid=Integer.parseInt(json.get("triggerId").toString());
	    	Integer deliverydaysaftertrigger=Integer.parseInt(json.get("DeliveryDaysAfterTigger").toString());
	    	Integer repeatfornumberofdays=Integer.parseInt(json.get("repeatForNoOfDays").toString());
	    	String subjectofmessage=json.get("subjectOfMessage");
	    	String bodyofmessage=json.get("bodyOfMessage");
	    	String remaindermessage=json.get("remainderOfMessage");
	    	String followupmessage=json.get("followupOfMessage");
	    	String status=json.get("status");
	    	Integer event_id=Integer.parseInt(json.get("eventId").toString());
	    	Integer msenttime=Integer.parseInt(json.get("msenttime").toString());
	    	Integer resulst=_blocksDao.insertpatientpathwayblocks(pwid,patientid,blockid,appointmentdate,blockappointmentparent,bname,btype,brow,messagesendat,messeagesentstatus,patientaccepteddate,phisecured,bcol,triggerid,deliverydaysaftertrigger,repeatfornumberofdays,subjectofmessage,bodyofmessage,remaindermessage,followupmessage,status,event_id,msenttime);
	    		response.setMessage("Scheduled data.");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  
  
  /**
   * Loading Patient Pathway Block Information by Event
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/getPatientblockinfo",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getPatientblockinfo(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer bid=Integer.parseInt(json.get("blockid").toString());
	 
	    	List resulst=_blocksDao.getBlockinfoforpatient(bid);
	    		response.setMessage("Scheduled data.");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  

  /**
   * Checking for patient pathway acceptance
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */

  @RequestMapping(value="/checkforpatientpathwayacceptance",method = RequestMethod.POST)
  public @ResponseBody JsonMessage checkforpatientpathwayacceptance(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer ptid=Integer.parseInt(json.get("patientid").toString());
	    	Integer phtid=Integer.parseInt(json.get("pathwayid").toString());
	    	String resulst=_blocksDao.checkforpatientpathway(ptid,phtid);
	    		response.setMessage("Scheduled data.");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  
  
  /**
   * Update Patient Pathway Block
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  
  @RequestMapping(value="/updatePatientPathwayblock",method = RequestMethod.POST)
  public @ResponseBody JsonMessage updatePatientPathwayblock(@RequestBody Map<String, String> json)
  {
	  JsonMessage response=new JsonMessage();
		 try 
		  {
			Integer ptid=Integer.parseInt(json.get("patientid").toString());
		    Integer bid=Integer.parseInt(json.get("blockid").toString());
		    String bmtype=json.get("mtype").toString();
		    String bmessage=json.get("messagetxt").toString();
			Integer results=_blocksDao.updatePatientblockmessagetxt(ptid, bid, bmtype, bmessage);
			response.setMessage("Pathway updated successfully");
			response.setData(results);
			response.setStatuscode(200);
			return response;
		  
		  }catch(Exception ex)
		 {
			  response.setMessage(ex.getMessage());
			  response.setStatuscode(204);
			  return response;
		 }
	 	 
  }
  
  
  /**
   * Pathway List by clinician
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/listpathwaysforclinicain",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listpathwaysforclinicain(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer pid=Integer.parseInt(json.get("clinicianid").toString());
	 
	    	List resulst=_blocksDao.getpathwaysbyclinicianid(pid);
	    		response.setMessage("Pathway List");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  

  /**
   * Events Listing by Pathway
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/listeventsforpathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listeventsforpathway(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer pid=Integer.parseInt(json.get("pathwayId").toString());
	    	List resulst=_blocksDao.getpathwayeventsbyid(pid);
	    		response.setMessage("Pathway List");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  /**
   *Edit Pathway Name
   * 
   * @Inputparam JsonObject
   * @return JsonObject
   */

  @RequestMapping(value="/updatepathwayname",method = RequestMethod.POST)
  public @ResponseBody JsonMessage updatepathwayname(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try 
	    { 
	    	
	    	
	    	Long pid=Long.parseLong(json.get("pid"));
	    	String putime=(json.get("utime"));
	    	
	    	
	    	String pname=(json.get("pname"));
	    	
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String updstring  = dateFormat.format(new Date());

	    		Integer results=_pathwayDao.updatepathwayname(pid,pname,updstring);
	    		
	    		response.setMessage("Pathway Updated.");
	    		response.setData(results);
	    		response.setStatuscode(200);
	    		return response;
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    }
	 	 
  }
  
  
  

  /**
  * Load Organization information by OrgId
  * 
  * @Inputparam JsonObject
  * @return JsonObject
  */
  
  @RequestMapping(value="/getorginfobypathwayid",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getorginfobypathwayid(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer pid=Integer.parseInt(json.get("pathwayid").toString());
	 
	    	List resulst=_pathwayDao.getorgnameforpathwayid(pid);
	    		response.setMessage("Pathway List");
	    		response.setData(resulst);
	    		response.setStatuscode(200);
	    		return response;
	    		
	    }
	    catch(Exception ex) 
	    {
	    	  response.setMessage(ex.getMessage());
	    	  response.setStatuscode(203);
	    	  return response;
	    
	    }
	 	 
  }
  
  
  
}