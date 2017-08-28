package com.engage.controller;

import com.engage.model.Pathway;
import com.engage.model.PathwayEvents;
import com.engage.model.Patient;
import com.engage.model.PatientPathway;
import com.engage.model.Twilio;
import com.engage.util.AdvancedEncryptionStandard;
import com.engage.util.JsonMessage;
import com.engage.util.UtilityFunctions;
import com.engage.dao.PathwayDao;
import com.engage.dao.PathwayEventsDao;
import com.engage.dao.PatientDao;
import com.engage.dao.PatientPathwayDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value="/api/v1")
public class PatientController {
	private static Logger log = LoggerFactory.getLogger(PatientController.class);
  @Autowired
  private PatientDao _patientDao;
  @Autowired
  private PatientPathwayDao _patientPathwayDao;
  @Autowired
  private PathwayDao _pathwayDao;
  @Autowired
  private PathwayEventsDao _pathwayEventsDao;
  //Add Patient 
  @RequestMapping(value="/addPatient",method = RequestMethod.POST)
  public @ResponseBody JsonMessage create(@RequestBody final Patient user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	PatientPathway patentPathway=new PatientPathway();
    	user.setStatus("Y");
    	//System.out.println(user.getEvents());
		long id=_patientDao.save(user);
		
		
		if(user.getPathwayId()!=0)
		{
	
		for(int i=0;i<user.getEvents().length;i++)
		{
			
			patentPathway.setPatientid(id);
			patentPathway.setEventId(user.getEvents()[i]);//so for set default event id as a 1 
			patentPathway.setTeamId(1);
			patentPathway.setPathwayId(user.getPathwayId());
			_patientPathwayDao.save(patentPathway);
			 
		}
		RestTemplate restTemplate = new RestTemplate();
		Map<String,Object> data1=new HashMap<String,Object>();	
//	 String results = restTemplate.getForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/patientpathwaycron", String.class);
	 String results = restTemplate.getForObject("http://localhost:8080/PathwayMicroservice/api/v1/patientpathwaycron", String.class);

	 
	
		
	
	 
	
		
		data1.put("toNumber", "+91"+user.getPhone());
//	 data1.put("toNumber", "+1"+user.getPhone());//for us
		String sms="Hi "+UtilityFunctions.toTitleCase(user.getFirstName())+", Congrats on completing your surgery! Your Doctor has set up a new text messaging program to help you with your recovery. Reply with Y to accept. Contact CaringOne anytime to stop this service.";
		data1.put("smsText",sms );
		
		System.out.println("Character length ####################");
		System.out.println(sms.length());
		System.out.println(sms);
		
//		String Str=restTemplate.postForObject("http://35.166.195.23:8080/ScheduleMicroservice/api/v1/sendSms", data1,String.class );
		String Str=restTemplate.postForObject("http://localhost:8080/ScheduleMicroservice/api/v1/sendSms", data1,String.class );
		
		}
		else
		{
			System.out.println("later remove in pathway zero");
		}

		

		//String Str=restTemplate.postForObject("http://localhost:8083/api/v1/sendSms", data1,String.class );
		
		response.setMessage("Patient saved successfully");
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
  //Edit Patient 
  @RequestMapping(value="/editPatient",method = RequestMethod.POST)
  public @ResponseBody JsonMessage update(@RequestBody final Patient user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {	PatientPathway patentPathway=new PatientPathway();
    	Patient patient=_patientDao.getById(user.getId());
    	if(patient.getId()>0)
    	{
    		
    		_patientDao.update(user);
    		_patientPathwayDao.deletePathEvents(user.getId());
    		for(int i=0;i<user.getEvents().length;i++)
    		{
    			
    			patentPathway.setPatientid(user.getId());
    			patentPathway.setEventId(user.getEvents()[i]);//so for set default event id as a 1 
    			patentPathway.setTeamId(1);
    			patentPathway.setPathwayId(user.getPathwayId());
    			_patientPathwayDao.save(patentPathway);
    		}
    		if(user.getEvents().length>0)
    		{

    			
    		}

    		response.setMessage("Patient updated successfully");
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("Patient doen't exists.");
    		response.setStatuscode(204);
    		return response;
    	}
    }
    catch(Exception ex) 
    {
    	  response.setMessage(ex.getMessage());
    	  response.setStatuscode(203);
    	  return response;
    }
  }
  //view patient
  @SuppressWarnings("unchecked")
@RequestMapping(value="/view_Patient",method = RequestMethod.POST)
  public @ResponseBody JsonMessage viewPatient(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	Patient patient=_patientDao.getById(json.get("id"));
    	if(patient.getId()>0)
    	{
    		ArrayList<Long>patientPathway= _patientPathwayDao.getPathwayById(patient.getId());
    		ArrayList<Long>events= _patientPathwayDao.getEventsById(patient.getId());
    		Map<String,Object> data=new HashMap<String,Object>();
    		data.put("patient",patient);
    		RestTemplate restTemplate = new RestTemplate();
			Map<String,Object> data1=new HashMap<String,Object>();
			data1.put("id", patientPathway);
			data1.put("evetIds", events);
//			Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/listPathwayEventForPatients", data1,Object.class );
			Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://localhost:8080/PathwayMicroservice/api/v1/listPathwayEventForPatients", data1,Object.class );
			
			data.put("pathwayInfo",pathway);
    		response.setMessage("Patient data");
    		response.setData(data);
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("Patient doen't exists.");
    		response.setStatuscode(204);
    		return response;
    	}
    }
    catch(Exception ex) 
    {
    	response.setMessage("Patient doen't exists.");
    	response.setStatuscode(203);
    	  return response;
    }
  }
  
  //view list of patients
@RequestMapping(value="/list_Patient",method = RequestMethod.POST)
  public @ResponseBody JsonMessage viewPatients(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    		List<Patient> patient=_patientDao.getAll(json.get("clinicianId"));
    		List<Object> patientsData=new ArrayList<Object>();
    		for(Patient p:patient)
    		{
    			ArrayList<Long>patientPathway= _patientPathwayDao.getPathwayById(p.getId());
        		ArrayList<Long>events= _patientPathwayDao.getEventsById(p.getId());
        		
    			///PatientPathway patientPathway= _patientPathwayDao.getByPatientId(p.getId());
        	//	Pathway pathway=_pathwayDao.getById(patientPathway.getPathwayId());
        	//	PathwayEvents pathwayEvents=_pathwayEventsDao.getById(patientPathway.getEventId());
        		Map<String,Object> data=new HashMap<String,Object>();
        		
        		data.put("patient",p);
        		RestTemplate restTemplate = new RestTemplate();
    			Map<String,Object> data1=new HashMap<String,Object>();
    			data1.put("id", patientPathway);
    			data1.put("evetIds", events);
//    			Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/listPathwayEventForPatients", data1,Object.class );
    			Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://localhost:8080/PathwayMicroservice/api/v1/listPathwayEventForPatients", data1,Object.class );
    			
    			data.put("pathwayInfo",pathway);
        		//data.put("pathwayInfo",pathway);
        		//data.put("eventInfo",pathwayEvents);
        		//data.put("patient",p);
        		patientsData.add(data);
    		}
    		response.setMessage("Patient list.");
    		response.setData(patientsData);
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
  //delete patient
  @RequestMapping(value="/delete_Patient",method = RequestMethod.POST)
  public @ResponseBody JsonMessage deletePatient(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	if(_patientDao.verifyId(json.get("id")).size()>0)
    	{Patient patient=_patientDao.getById(json.get("id"));
    	
    		_patientDao.delete(patient);
    		response.setMessage("Patient deleted successfully");
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("Patient doen't exists.");
    		response.setStatuscode(204);
    		return response;
    	}
    }
    catch(Exception ex) 
    {
    	  response.setMessage(ex.getMessage());
    	  response.setStatuscode(203);
    	  return response;
    }
  }
  @RequestMapping(value="/getPatientsCountByPathwayId",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getPatientsCountByPathwayId(@RequestBody Map<String, Long> json) 
  {
	Integer count=0;
	JsonMessage response=new JsonMessage();
    try 
    {	
    		count=_patientPathwayDao.getPatientCount(json.get("pathwayId"));
    		response.setData(count);
    		response.setStatuscode(200);
      	  return response;
    }
    catch(Exception ex) 
    {
    	
    	response.setData(count);
		response.setStatuscode(200);
  	  return response;
    }
  }
  @RequestMapping(value="/getPatientsPathwayIdAndEventId",method = RequestMethod.POST)
  public @ResponseBody Patient getPatientsPathwayIdAndEventId(@RequestBody Map<String, Long> json) 
  {
	  Patient patient=null;
	Integer count=0;
	JsonMessage response=new JsonMessage();
    try 
    {	
    	System.out.print("Patient Information by Pathway Id and Event Id");
    	 Long pthid=json.get("pathwayId");
    	 Long enid=json.get("eventId");
    	 System.out.println("Iput Params@@@@@@@@@@@@@@@@");
    	 System.out.println(pthid);
    	 System.out.println(enid);
    		count=_patientPathwayDao.getPatientInfo(pthid, enid);
    		System.out.println("******************&&&&&&&&&");
    		System.out.println(count);
    		 patient=_patientDao.getById(count);
    		response.setData(patient);
    		response.setStatuscode(200);
      	  return patient;
    }
    catch(Exception ex) 
    {
    	
    	response.setData(count);
		response.setStatuscode(200);
  	  return patient;
    }
  }
  
  //lakhsmi get patient by phone
  
  @RequestMapping(value="/getPatientByphone",method = RequestMethod.POST)
  public  @ResponseBody JsonMessage  getPatientByphone(@RequestBody  Map<String, Integer> json ) 
  {
	  JsonMessage response=new JsonMessage();
	  try
	  {
		  Integer pid=json.get("patientid");
		  System.out.println("This is Patient^^^^^^^^^^^^^^^^");
		  System.out.println(pid);
		 String ph= _patientDao.patientPhone(pid);
		  response.setData(ph);
			response.setStatuscode(200);
			return response; 
	  }catch(Exception ex)
	 {
		  response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response; 
	 }
	 	
  }
  //En of get patient by phone
  
  //Lakshmi Get Patientbydob Queries
  @RequestMapping(value="/getPatientBydob",method = RequestMethod.POST)
  public  @ResponseBody JsonMessage  getPatientBydob(@RequestBody  Map<String, String> json ) 
  {
	  JsonMessage response=new JsonMessage();
	  try
	  {
		  String pdob=json.get("dob");
		  System.out.println("This is Patient^^^^^^^^^^^^^^^^");
		  System.out.println(pdob);
		 int result= _patientDao.verifyPatientInfobydob(pdob);
		  response.setData(result);
			response.setStatuscode(200);
			return response; 
	  }catch(Exception ex)
	 {
		  response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response; 
	 }
	 	
  }
  
  
  @RequestMapping(value="/twiliorReplayAPI",method = RequestMethod.POST)
  public ResponseEntity<String> receiveBody(@RequestParam("From") String From, @RequestParam("Body") String Body)
  {
	  JsonMessage response=new JsonMessage();
	  HttpHeaders responseHeaders = new HttpHeaders();
	  try
	  {

		  String ph=From;
		  ph=ph.substring(3);//for India/
//		  ph=ph.substring(2);//for US
		  System.out.println("This is the input phone");
		  System.out.println(ph);
	 Patient patient=_patientDao.getByPhone(ph);
	 
		
	 if(patient.getId()>0)
	 {
		 if(_patientDao.verifyPatientInfo(patient.getId())>0)
		 {
//			 	response.setMessage("Patient acceptence info already updated");
//				response.setStatuscode(200);
//				return response; 
			 return new ResponseEntity<String> ("someResponse", null);
		 }
		 ArrayList<Long>patientPathway= _patientPathwayDao.getPathwayById(patient.getId());
		 System.out.println("Lakhsmi Pagag$$$$$$$$$$$$$$");
		 System.out.println(Integer.parseInt(patientPathway.get(0).toString()));
	
		 
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 
		 System.out.println(dateFormat.format(new Date())); // will print like 2014-02-20
		  String dd=dateFormat.format(new Date());
		 
		 System.out.println("Lakhsmi Update");
		 System.out.println(dd);
		 _patientDao.updatePatientInfofprpathway(patient.getId(),  Integer.parseInt(patientPathway.get(0).toString()), "Y", dd, Body.toUpperCase());
		 RestTemplate restTemplate = new RestTemplate();
			Map<String,Object> data1=new HashMap<String,Object>();	
//		 String results = restTemplate.getForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/patientpathwaycron", String.class);
		 String results = restTemplate.getForObject("http://localhost:8080/PathwayMicroservice/api/v1/patientpathwaycron", String.class);

			response.setMessage("Thank you for joining. You will start receiving messages");
			response.setStatuscode(200);
			
			return new ResponseEntity<String> ("Thank you for joining. You will start receiving messages.", responseHeaders,HttpStatus.OK);
	 }
	 else
	 {
		 return new ResponseEntity<String> ("Thank you for your reply.", responseHeaders,HttpStatus.OK);
//		 response.setMessage("Patient acceptence info not updated successfully");
//			response.setStatuscode(200);
//			return response; 
	 }
	  }catch(Exception ex)
	 {
		  return new ResponseEntity<String> ("Error", responseHeaders,HttpStatus.OK);
//		  response.setMessage(ex.getMessage());
//			response.setStatuscode(200);
//			return response; 
	 }
	 	
  }
  
  
} // class UserController