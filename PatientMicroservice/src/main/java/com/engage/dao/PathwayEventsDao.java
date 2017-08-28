package com.engage.dao;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.engage.model.PathwayEvents;

@Repository
@Transactional
public class PathwayEventsDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public long save(PathwayEvents patient) {
	    long id=(long) getSession().save(patient);
	    return id;
	  }
	  
	  public void delete(PathwayEvents patient) {
	    getSession().delete(patient);
	    return;
	  }
	  
	  @SuppressWarnings("unchecked")
	  public List<PathwayEvents> getAll() {
		  List<PathwayEvents> praticenames=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(PathwayEvents.class);
			  crit.add(Restrictions.eq("status","Y"));
			  
			  praticenames=crit.list();
			  
			  return praticenames;
		  }catch(Exception ex)
		  {
			  return praticenames;
		  }
	  }
	  
//	  @SuppressWarnings("unchecked")
	public List<PathwayEvents> verifyId(long id) {
		  List<PathwayEvents> patient=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(PathwayEvents.class);
			  crit.add(Restrictions.eq("id",id));
			  patient= crit.list();
			  
			  return patient;
		  }catch(Exception ex)
		  {
			  return patient;
		  }
	    
	  }

	  public PathwayEvents getById(long id) {
		  return (PathwayEvents) getSession().get(PathwayEvents.class, id);
		 
	  }
	 

	  public void update(PathwayEvents user) {
	    getSession().update(user);
	    return;
	  }

}
