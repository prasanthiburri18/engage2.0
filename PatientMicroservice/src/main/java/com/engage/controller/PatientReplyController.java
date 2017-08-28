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

/**
* Patient MicroServices for PatientPathway API Operations
* 
* @author  StartUP Labs
* @version 1.0
* 
*/
@RestController
@RequestMapping(value="/api/v1")
public class PatientReplyController {
	private static Logger log = LoggerFactory.getLogger(PatientReplyController.class);
  @Autowired
  private PatientDao _patientDao;
  @Autowired
  private PatientPathwayDao _patientPathwayDao;
  @Autowired
  private PathwayDao _pathwayDao;
  @Autowired
  private PathwayEventsDao _pathwayEventsDao;
  //Add Patient 
  /**
   * patient Reply Method
   * 
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/patientreplay",method = RequestMethod.POST)
  public ResponseEntity<String> receiveBody(@RequestParam("From") String From, @RequestParam("Body") String Body)
  {
	  JsonMessage response=new JsonMessage();
	  HttpHeaders responseHeaders = new HttpHeaders();
	  try
	  {

		  String ph=From;
//		  ph=ph.substring(3);//for India/
		  ph=ph.substring(2);//for US
		 
	 Patient patient=_patientDao.getByPhone(ph);
	 
		
	 if(patient.getId()>0)
	 {
		 if(_patientDao.verifyPatientInfo(patient.getId())>0)
		 {
			 	
			 return new ResponseEntity<String> ("someResponse", null);
		 }
		 ArrayList<Long>patientPathway= _patientPathwayDao.getPathwayById(patient.getId());
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String dd=dateFormat.format(new Date());
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

	 }
	  }catch(Exception ex)
	 {
		  return new ResponseEntity<String> ("Error", responseHeaders,HttpStatus.OK);

	 }
	 	
  }
  
}