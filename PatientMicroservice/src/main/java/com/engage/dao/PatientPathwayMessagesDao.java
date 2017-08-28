package com.engage.dao;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.engage.model.PatientPathwayMessages;

@Repository
@Transactional
public class PatientPathwayMessagesDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }
	  public long save(PatientPathwayMessages patient) {
	    long id=(long) getSession().save(patient);
	    return id;
	  }
	  public void delete(PatientPathwayMessages patient) {
	    getSession().delete(patient);
	    return;
	  }
	  @SuppressWarnings("unchecked")
	  public List<PatientPathwayMessages> getAll() {
		  List<PatientPathwayMessages> praticenames=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(PatientPathwayMessages.class);
			  praticenames=crit.list();
			  return praticenames;
		  }catch(Exception ex)
		  {
			  return praticenames;
		  }
	  }
	  
//	  @SuppressWarnings("unchecked")
	public List<PatientPathwayMessages> verifyId(long id) {
		  List<PatientPathwayMessages> patient=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(PatientPathwayMessages.class);
			  crit.add(Restrictions.eq("id",id));
			  patient= crit.list();
			  
			  return patient;
		  }catch(Exception ex)
		  {
			  return patient;
		  }
	    
	  }
	  public PatientPathwayMessages getById(long id) {
		  return (PatientPathwayMessages) getSession().get(PatientPathwayMessages.class, id);
		 
	  }
	  public void update(PatientPathwayMessages user) {
	    getSession().update(user);
	    return;
	  }

}
