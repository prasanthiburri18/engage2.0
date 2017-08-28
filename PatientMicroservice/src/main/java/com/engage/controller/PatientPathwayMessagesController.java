package com.engage.controller;

import com.engage.util.AdvancedEncryptionStandard;
import com.engage.util.JsonMessage;
import com.engage.dao.PatientPathwayMessagesDao;
import com.engage.model.PatientPathwayMessages;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



/**
* Patient MicroServices for PatientPathway API Operations
* 
* @author  StartUP Labs
* @version 1.0
*/

@RestController
@RequestMapping(value="/api/v1")
public class PatientPathwayMessagesController {
	  private static Logger log = LoggerFactory.getLogger(PatientPathwayMessagesController.class);
  @Autowired
  private PatientPathwayMessagesDao _patientPathwayMessagesDao;
  /**
   * Save PatientPathway Message Method
   * 
   * @Inputparam Patient JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/addPatientPathwayMessages",method = RequestMethod.POST)
  public @ResponseBody JsonMessage create(@RequestBody final PatientPathwayMessages user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
		long id=_patientPathwayMessagesDao.save(user);
		response.setMessage("PatientPathwayMessages saved successfully");
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
   * Edit PatientPathway Message Method
   * 
   * @Inputparam Patient JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/editPatientPathwayMessages",method = RequestMethod.POST)

  public @ResponseBody JsonMessage update(@RequestBody final PatientPathwayMessages user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	PatientPathwayMessages patient=_patientPathwayMessagesDao.getById(user.getId());
    	if(patient.getId()>0)
    	{
    		_patientPathwayMessagesDao.update(patient);
    		response.setMessage("PatientPathwayMessages saved successfully");
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("PatientPathwayMessages doen't exists.");
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
  /**
   * View PatientPathway Message Method
   * 
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/view_PatientPathwayMessages",method = RequestMethod.POST)

  public @ResponseBody JsonMessage viewPatient(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	PatientPathwayMessages patient=_patientPathwayMessagesDao.getById(json.get("id"));
    	if(patient.getId()>0)
    	{
    		response.setMessage("PatientPathwayMessages data");
    		response.setData(patient);
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("PatientPathwayMessages doen't exists.");
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
  /**
   * List PatientPathway Message Method
   * 
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  
  @RequestMapping(value="/list_PatientPathwayMessages",method = RequestMethod.POST)

  public @ResponseBody JsonMessage viewPatients(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    		List<PatientPathwayMessages> patient=_patientPathwayMessagesDao.getAll();
    		response.setMessage("PatientPathwayMessages doen't exists.");
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
   * Delete PatientPathway Message Method
   * 
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/delete_PatientPathwayMessages",method = RequestMethod.POST)

  public @ResponseBody JsonMessage deletePatient(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	
    	if(_patientPathwayMessagesDao.verifyId(json.get("id")).size()>0)
    	{PatientPathwayMessages patient=_patientPathwayMessagesDao.getById(json.get("id"));
    	patient.setStatus("Y");
    		_patientPathwayMessagesDao.update(patient);
    		response.setMessage("PatientPathwayMessages saved successfully");
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("PatientPathwayMessages doen't exists.");
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
}