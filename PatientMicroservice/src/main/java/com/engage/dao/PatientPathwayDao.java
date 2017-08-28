package com.engage.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.engage.model.Patient;
import com.engage.model.PatientPathway;
import com.engage.util.AdvancedEncryptionStandard;

@Repository
@Transactional
public class PatientPathwayDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public long save(PatientPathway patient) {
	    long id=(long) getSession().save(patient);
	    return id;
	  }
	  
	  public void delete(PatientPathway patient) {
	    getSession().delete(patient);
	    return;
	  }
	  public void deletePathEvents(Long patientid) {
		  Query query = getSession().createQuery("delete PatientPathway where patientid = :patientid");
		  query.setParameter("patientid",patientid);
		  int result = query.executeUpdate();
		  }
	  
	  @SuppressWarnings("unchecked")
	  public List<PatientPathway> getAll() {
		  List<PatientPathway> praticenames=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(PatientPathway.class);
			  praticenames=crit.list();
			  return praticenames;
		  }catch(Exception ex)
		  {
			  return praticenames;
		  }
	  }
	  
//	  @SuppressWarnings("unchecked")
	public List<PatientPathway> verifyId(long id) {
		  List<PatientPathway> patient=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(PatientPathway.class);
			  crit.add(Restrictions.eq("id",id));
			  patient= crit.list();
			  
			  return patient;
		  }catch(Exception ex)
		  {
			  return patient;
		  }
	  }

	  public PatientPathway getById(long id) {
		 PatientPathway patient=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(PatientPathway.class);
			  crit.add(Restrictions.eq("id",id));
			  patient= (PatientPathway) crit.uniqueResult();
			  
			  return patient;
		  }catch(Exception ex)
		  {
			  return patient;
		  }
		 }
	  @SuppressWarnings({ "unchecked", "null" })
	public ArrayList<Long> getPathwayById(long id) {
		  ArrayList<Long> patientPathway=null;
		  
		  try
		  {
			  Criteria crit = getSession().createCriteria(PatientPathway.class);
			  crit.add(Restrictions.eq("patientid",id));
			  crit.setProjection(Projections.max("pathwayId"));
			  patientPathway= (ArrayList<Long>) crit.list();
			
			  return patientPathway;
		  }catch(Exception ex)
		  {
			  System.out.println("pathway:"+ex);
			  return patientPathway;
		  }
		 }
	  @SuppressWarnings("unchecked")
	public ArrayList<Long> getEventsById(long id) {
		  ArrayList<Long>events=null;
		  
		  try
		  {
			  Criteria crit = getSession().createCriteria(PatientPathway.class);
			  crit.add(Restrictions.eq("patientid",id));
			  crit.setProjection(Projections.property("eventId"));
			  events= (ArrayList<Long>) crit.list();
			  return events;
		  }catch(Exception ex)
		  {System.out.println(ex);
			  return events;
		  }
		 }
	  public void update(PatientPathway user) {
	    getSession().update(user);
	    return;
	  }
	  
	  public int getPatientCount(long id) {
		  int count=0;
		  try
		  {
			
			  String hql = "SELECT count(DISTINCT patient_id,pathway_id) FROM patientdb.qc_patient_pathway,patientdb.qc_patient where patientdb.qc_patient_pathway.pathway_id="+id+" and patientdb.qc_patient.id=patientdb.qc_patient_pathway.patient_id";
			  SQLQuery query=getSession().createSQLQuery(hql);
			  List distCountList = query.list();
		      if(distCountList != null)
		      {
		    	count=  Integer.parseInt(distCountList.get(0).toString());
		      }
		  return count;
		  }catch(Exception ex)
		  {
			  System.out.println( ex.getMessage());
			  return count;
		  }
	
		 
	  }
	public int getPatientInfo(Long pathwwayid,Long eventid) {
		  Integer count=0;
		  try
		  {
	  String hql = "Select a.patientid FROM PatientPathway as a where a.pathwayId="+pathwwayid+" and a.eventId="+eventid+"";
		  return count;
		  }catch(Exception ex)
		  {System.out.println( ex.getMessage());
			  return count;
		  }
	  }
}
