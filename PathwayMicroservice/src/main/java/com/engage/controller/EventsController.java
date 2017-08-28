package com.engage.controller;




import com.engage.dao.EventsDao;

import com.engage.model.Events;
import com.engage.model.Pathway;
import com.engage.util.JsonMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
* Pathway MicroServices for Events API Operations
* 
* @author  StartUP Labs
* @version 1.0
* 
*/
@RestController
@RequestMapping(value="/api/v1")
public class EventsController {
	private static Logger log = LoggerFactory.getLogger(EventsController.class);
  @Autowired
  private EventsDao _eventsDao;
/**
 * Add Event Method
 * @Inutparam eventObject
 * @return JsonObject
 */
  @RequestMapping(value="/addEvent",method = RequestMethod.POST)
  public @ResponseBody JsonMessage addEvent(@RequestBody final Events events) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
		 Boolean isPathway= _eventsDao.getByEventName(events.getEventName(),events.getPathwayId());
		 if(isPathway==true)
		 {
			 response.setMessage("This event already exists");
				response.setStatuscode(208);
				return response;
		 
		 }else
		 {
			 	events.setStatus("Y");
				Long id= _eventsDao.save(events);
				response.setMessage("Events registered successfully");
				response.setData(id);
				response.setStatuscode(200);
				return response;
		 }
	  
	  }catch(Exception ex)
	 {
		  response.setMessage("Events not registered");
		  response.setStatuscode(204);
		  return response;
	 }
  }

  /**
   * Delete Event Method
   * @Inutparam JsonObject
   * @return JsonObject
   */

  @RequestMapping(value="/deleteEvents",method = RequestMethod.POST)
  public @ResponseBody JsonMessage deleteEvents(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 Events events=_eventsDao.getById(Long.parseLong(json.get("id")));
		 if(events.getId()>0)
		  {
			 _eventsDao.delete(events);
			  response.setMessage("Events deleted successfully.");
			  response.setStatuscode(200);
			  return response;
		  }else
		  {
			  response.setMessage("Events not registered.");
			  response.setStatuscode(204);
			  return response;
		  }
	 }catch (Exception e) {
		 response.setMessage("Events not registered.");
		  response.setStatuscode(204);
		  return response;
	}
	 	 
  }

  
  /**
   * Edit Event Method
   * @Inutparam eventObject
   * @return JsonObject
   */
  @RequestMapping(value="/updateEvents",method = RequestMethod.POST)
  public @ResponseBody JsonMessage updateEvents(@RequestBody final Events events) 
  {
	  JsonMessage response=new JsonMessage();
		 try 
		  {
		  
			_eventsDao.update(events);
			response.setMessage("Events updated successfully");
			response.setStatuscode(200);
			return response;
		  
		  }catch(Exception ex)
		 {
			  response.setMessage("Events not registered");
			  response.setStatuscode(204);
			  return response;
		 }
	 	 
  }
  /**
   * List Event Method
   * @Inutparam JsonObject
   * @return JsonObject
   */
  
  @RequestMapping(value="/listEvents",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listEvents(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try 
	    {
	    
	    		List<Events> events=_eventsDao.getAll(Long.parseLong(json.get("pathwayId")));
	    		
	    		response.setMessage("Events list.");
	    		response.setData(events);
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
   * List Event Method
   * Listing Events For Pathway
   * @Inutparam JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/listEventsbyPathwayId",method = RequestMethod.POST)
  public @ResponseBody JsonMessage listEventsbyPathwayId(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	    try 
	    {
	    
	    		List<Object> events=_eventsDao.getEventnamesbyPathwayId(Long.parseLong(json.get("pathwayId")));
	     		response.setMessage("Events Names.");
	    		response.setData(events);
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