package com.engage.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.engage.model.Patient;
//import com.engage.model.ScheduleJson;
import com.engage.util.AdvancedEncryptionStandard;

@Repository
@Transactional
public class PatientDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public long save(Patient patient) {
	    long id=(long) getSession().save(patient);
	    return id;
	  }
	  
	  public void delete(Patient patient) {
	    getSession().delete(patient);
	    return;
	  }
	  
	  @SuppressWarnings("unchecked")
	  public List<Patient> getAll(long clinicianId) {
		  List<Patient> praticenames=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Patient.class);
			  crit.add(Restrictions.eq("status","Y"));
			  crit.add(Restrictions.eq("orgId",clinicianId));
			  praticenames=crit.list();
			  
			  return praticenames;
		  }catch(Exception ex)
		  {
			  return praticenames;
		  }
	  }
	  
//	  @SuppressWarnings("unchecked")
	public List<Patient> verifyId(long id) {
		  List<Patient> patient=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Patient.class);
			  crit.add(Restrictions.eq("id",id));
			  patient= crit.list();
			  
			  return patient;
		  }catch(Exception ex)
		  {
			  return patient;
		  }
	    
	  }

	  public Patient getById(long id) {
		  return (Patient) getSession().get(Patient.class, id);
		 
	  }
	  
	//Get Patient First data
	  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getPathwayFirstMessageforpatient(int pathwayid) {
	 	  List<Object> output=new ArrayList<Object>();
	 	  try{
	 			
	 	 String sql = "Select pathwaydb.qc_pathway_block.* from pathwaydb.qc_pathway_block WHERE pathwaydb.qc_pathway_block.pathway_id ="+pathwayid+" and pathwaydb.qc_pathway_block.block_name='Sign Up'";				
		  SQLQuery query=getSession().createSQLQuery(sql);
	 	  List<Object[]> results = query.list();
 	  ArrayList blockresinner = new ArrayList();
 	  Object[] obj = new Object[] {};
	 	  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
 	  for(Object[] row : results){
 		  ArrayList blockres = new ArrayList();
	 		  Map<String, String> assoc = new HashMap<String, String>();
	 		  blockresinner.add(row[12].toString());
	 		}
	 	    return blockresinner;
	 	    
	 	    
	 	  }catch(Exception ex)
	 	  {
	 		  System.out.println(ex.getMessage());
	 		  
	 		  return output;
	 	  }
	 	  }

	  public Patient getByPhone(String id) {
		  List<Patient> patient=null;
		  Patient p=new Patient();
		  Criteria crit = getSession().createCriteria(Patient.class);
		  patient =  crit.list();
		 for (Patient patient2 : patient) {
			 String phone=patient2.getPhone().replace("-", "");
		
			if(phone.equals(id))
			{
		
				p=patient2;
			}
		}
		  return p;
	  }

	  public void update(Patient user) {
		    getSession().update(user);
		    return;
		  }
	  public int updatePatientInfofprpathway(long patient_id,int pathway_id,String status,String accepteddate,String accept) {
		
		  String sql = "INSERT INTO pathwaydb.qc_patient_pathway_info (patient_id, pathway_id, status, accepteddate, accept) VALUES ("+patient_id+","+pathway_id+",'Y','"+accepteddate+"','"+accept+"')";
			  SQLQuery query=getSession().createSQLQuery(sql);
			  int statuscode=query.executeUpdate();
		  return statuscode;
			  
		  }
	  public int updatePatientInfo(long patient_id,int pathway_id,String status,Date accepteddate,String accept) {
		
		  String sql = "INSERT INTO pathwaydb.qc_patient_pathway_info (patient_id, pathway_id, status, accepteddate, accept) VALUES ("+patient_id+","+pathway_id+",'Y','"+accepteddate+"','"+accept+"')";
			  SQLQuery query=getSession().createSQLQuery(sql);
			  int statuscode=query.executeUpdate();
			  return statuscode;
			  
		  }
	  public int verifyPatientInfo(long patientid) {
		  String sql = "SELECT  pathwaydb.qc_patient_pathway_info.patient_id FROM pathwaydb.qc_patient_pathway_info where pathwaydb.qc_patient_pathway_info.patient_id="+patientid+"";
			  SQLQuery query=getSession().createSQLQuery(sql);
			  int statuscode= (int) query.list().size();
			  return statuscode;
		  }
	  
	  
	  //Verify Patient By DOB for PHI Secured Info
	  public int verifyPatientInfobydob(String dob) {
		  String sql = "SELECT  patientdb.qc_patient.dob FROM patientdb.qc_patient where patientdb.qc_patient.dob='"+dob+"'";
		  SQLQuery query=getSession().createSQLQuery(sql);
		  int statuscode= (int) query.list().size();
	  return statuscode;
		  }
	  
	  
	  
	  public String patientPhone(Integer id) {
		  
		  try
		  {
		  
		  String sql = "SELECT  patientdb.qc_patient.* FROM patientdb.qc_patient where patientdb.qc_patient.id="+id+"";
			  SQLQuery query=getSession().createSQLQuery(sql);
				  List<Object[]> presults= query.list();
			  for(Object[] pw: presults){
				  
			return AdvancedEncryptionStandard.decrypt(pw[6].toString());
			  }
			  return "No Data";
		  }
		  catch(Exception ex)
			 {
				
					return ex.getMessage(); 
			 }
		  }
	  
	  public int updatePatientAccept(long patientid) {
		  String sql = "UPDATE pathwaydb.qc_patient_pathway_info SET accepteddate='"+new Date()+"' WHERE patient_id="+patientid+"";
		  SQLQuery query=getSession().createSQLQuery(sql);
			  int statuscode= (int) (long)query.uniqueResult();
			  return statuscode;
		  }
	  
	  

}
