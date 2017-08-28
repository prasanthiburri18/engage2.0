package com.engage.controller;

import com.engage.util.JsonMessage;
import com.engage.dao.PatientPathwayDao;
import com.engage.model.PatientPathway;

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
* 
*/

@RestController

@RequestMapping(value="/api/v1")
public class PatientPathwayController {
	  private static Logger log = LoggerFactory.getLogger(PatientPathwayController.class);
  @Autowired
  private PatientPathwayDao _patientPathwayDao;
  /**
   * Add PatientPathway Method
   * 
   * @Inputparam PatientPathway JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/addPatientPathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage createPatientPathway(@RequestBody final PatientPathway user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
		long id=_patientPathwayDao.save(user);
		response.setMessage("PatientPathway saved successfully");
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
   * Edit PatientPathway Method
   * 
   * @Inputparam PatientPathway JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/editPatientPathway",method = RequestMethod.POST)

  public @ResponseBody JsonMessage update(@RequestBody final PatientPathway user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	PatientPathway patient=_patientPathwayDao.getById(user.getId());
    	if(patient.getId()>0)
    	{
    		_patientPathwayDao.update(patient);
    		response.setMessage("PatientPathway saved successfully");
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("PatientPathway doen't exists.");
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
   * View PatientPathway Method
   * 
   * @Inputparam PatientPathway JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/view_PatientPathway",method = RequestMethod.POST)
  public @ResponseBody JsonMessage viewPatient(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	PatientPathway patient=_patientPathwayDao.getById(json.get("id"));
    	if(patient.getId()>0)
    	{
    		response.setMessage("PatientPathway data");
    		response.setData(patient);
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("PatientPathway doen't exists.");
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
   * List PatientPathway Method
   * 
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  
  @RequestMapping(value="/list_PatientPathway",method = RequestMethod.POST)

  public @ResponseBody JsonMessage viewPatients(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    		List<PatientPathway> patient=_patientPathwayDao.getAll();
    		response.setMessage("PatientPathway doen't exists.");
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
   * Delete PatientPathway Method
   * 
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/delete_PatientPathway",method = RequestMethod.POST)

  public @ResponseBody JsonMessage deletePatient(@RequestBody Map<String, Long> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	if(_patientPathwayDao.verifyId(json.get("id")).size()>0)
    	{
    		PatientPathway patient=_patientPathwayDao.getById(json.get("id"));
    		_patientPathwayDao.delete(patient);
    		response.setMessage("PatientPathway saved successfully");
    		response.setStatuscode(200);
    		return response;
    	}
    	else
    	{
    		response.setMessage("PatientPathway doen't exists.");
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