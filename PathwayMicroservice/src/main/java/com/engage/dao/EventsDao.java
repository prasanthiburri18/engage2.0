package com.engage.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.engage.model.Events;
import com.engage.model.ScheduleJson;

@Repository
@Transactional
public class EventsDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public long save(Events pathwayEvents) {
	    long id=(long) getSession().save(pathwayEvents);
	    return id;
	  }
	  
	  public void delete(Events pathwayEvents) {
	    getSession().delete(pathwayEvents);
	    return;
	  }
	  
	  @SuppressWarnings("unchecked")
	  public List<Events> getAll(long pathwayId) {
		  List<Events> pathwayEvents=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Events.class);
			  crit.add(Restrictions.eq("pathwayId",pathwayId));
//			  crit.add(Restrictions.eq("status", "Y"));
			  
			  pathwayEvents=crit.list();
			  
			  return pathwayEvents;
		  }catch(Exception ex)
		  {
			  return pathwayEvents;
		  }
	  }
	  @SuppressWarnings("unchecked")
	  public List<Events> getAllEvents(ArrayList<Long> events) {
		  List<Events> pathwayEvents=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Events.class);
			  crit.add(Restrictions.in("id", events));

//			  crit.add(Restrictions.eq("status", "Y"));
			  
			  pathwayEvents=crit.list();
			  
			  return pathwayEvents;
		  }catch(Exception ex)
		  {
			  return pathwayEvents;
		  }
	  }
	  public Boolean getByEventName(String eventName,Long pathwayId) {
		  Boolean pathway=false;
		  try
		  {
			  
			  Criteria crit = getSession().createCriteria(Events.class);
			  crit.add(Restrictions.eq("eventName",eventName));
			  crit.add(Restrictions.eq("pathwayId",pathwayId));
			  if(crit.list().size()>0)
			  {
				 pathway=true;
			  }
		    return pathway;
		  }catch(Exception e)
		  {
			  System.out.println(e.getMessage());
			  return pathway;
		  }
		 
	  }
//	  @SuppressWarnings("unchecked")
	public List<Events> verifyId(long id) {
		  List<Events> pathwayEvents=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Events.class);
			  crit.add(Restrictions.eq("id",id));
			  pathwayEvents= crit.list();
			  
			  return pathwayEvents;
		  }catch(Exception ex)
		  {
			  return pathwayEvents;
		  }
	    
	  }

	  public Events getById(long id) {
		  return (Events) getSession().get(Events.class, id);
		 
	  }
	 

	  public void update(Events pathwayEvents) {
	    getSession().update(pathwayEvents);
	    return;
	  }
	  
	  //Get the event names by pathway Id.
	  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getEventnamesbyPathwayId(long pid) {
		  List<Object> scheduledData=new ArrayList<Object>();
		  ArrayList blockres = new ArrayList();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_event.* from pathwaydb.qc_pathway_event WHERE pathwaydb.qc_pathway_event.pathway_id ="+pid+"";				
		
		  SQLQuery query=getSession().createSQLQuery(sql);
		  List<Object[]> results = query.list();
		  ArrayList blockresinner = new ArrayList();
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
	 
		  for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();
			 
			 
			  Map<String, String> assoc = new HashMap<String, String>();
			  assoc.put("id",row[0].toString());
			  assoc.put("name",row[2].toString());
			
			  blockres.add(assoc);
			}
		    return blockres;
		    
		    
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  
			  return scheduledData;
		  }
		  }
	  
	  
	  
	  

}
