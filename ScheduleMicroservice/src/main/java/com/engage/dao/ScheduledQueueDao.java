package com.engage.dao;

import java.sql.Timestamp;
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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.engage.model.ScheduleJson;
import com.engage.model.ScheduledQueue;
import com.engage.util.AdvancedEncryptionStandard;
@Repository
@Transactional
public class ScheduledQueueDao {
	
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public String save(ScheduledQueue scheduledQueue) {
	    String id= (String) getSession().save(scheduledQueue);
	    return id;
	  }
	  
	  public void delete(ScheduledQueue scheduledQueue) {
	    getSession().delete(scheduledQueue);
	    return;
	  }
	  
	  @SuppressWarnings("unchecked")
	  public List<ScheduledQueue> getAll(long pathwayId) {
		  List<ScheduledQueue> scheduledQueues=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(ScheduledQueue.class);
			  scheduledQueues=crit.list();
			 return scheduledQueues;
		  }catch(Exception ex)
		  {
			  return scheduledQueues;
		  }
	  }
	
//	  @SuppressWarnings("unchecked")
	public List<ScheduledQueue> verifyId(long id) {
		  List<ScheduledQueue> scheduledQueues=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(ScheduledQueue.class);
			  crit.add(Restrictions.eq("id",id));
			  scheduledQueues= crit.list();
			  
			  return scheduledQueues;
		  }catch(Exception ex)
		  {
			  return scheduledQueues;
		  }
	    
	  }

	  public ScheduledQueue getById(long id) {
		  return (ScheduledQueue) getSession().get(ScheduledQueue.class, id);
		 
	  }
	 

	  public void update(ScheduledQueue scheduledQueues) {
	    getSession().update(scheduledQueues);
	    return;
	  }
	  
 public String patientinfobyPhone(Integer id) {
		  
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
 
 public int updatePatientmessagestatus(int id) {
	 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	 Long ctime=timestamp.getTime();
	  String sql = "UPDATE pathwaydb.qc_pathway_patient_blocks SET message_status=:message_status,msenttime=:msenttime WHERE id=:id";
	  SQLQuery query=getSession().createSQLQuery(sql);
		  query.setParameter("message_status", "sent");
		  query.setLong("msenttime", ctime);
	        query.setLong("id", id);
	        int statuscode = query.executeUpdate();
			  return statuscode;
	  }
 
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getBlockcrondataexecute() {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.message_send_at = CURDATE();";				
		  SQLQuery query=getSession().createSQLQuery(sql);
		  List<Object[]> results = query.list();
		  ArrayList blockresinner = new ArrayList();
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
		  for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();
			  ArrayList blockres = new ArrayList();
		  Map<String, String> assoc = new HashMap<String, String>();
			  blockres.add(row[0].toString());
			  blockres.add(row[1].toString());
			  blockres.add(row[2].toString());
			  blockres.add(row[3].toString());
			  blockres.add(row[4].toString());
			  blockres.add(row[5].toString());
			  blockres.add(row[6].toString());
			  blockres.add(row[7].toString());
			  blockres.add(row[8].toString());
			  blockres.add(row[9].toString());
			  blockres.add(row[10].toString());
			  blockres.add(row[11].toString());
			  blockres.add(row[12].toString());
			  blockres.add(row[13].toString());
			  blockres.add(row[14].toString());
			  blockres.add(row[15].toString());
			  blockres.add(row[16].toString());
			  blockres.add(row[17].toString());
			  blockres.add(row[18].toString());
			  blockres.add(row[19].toString());
			  blockres.add(row[20].toString());
			  blockres.add(row[21].toString());
			  blockres.add(row[22].toString());
			  blockres.add(row[23].toString());
			  blockresinner.add(blockres);
			}
		    return blockresinner;
		       
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  return scheduledData;
		  }
		  }
	  
	    
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getFirstdayBlockcrondataexecute() {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.block_pos_row=1 and pathwaydb.qc_pathway_patient_blocks.message_send_at = CURDATE();";				
		 SQLQuery query=getSession().createSQLQuery(sql);
		  List<Object[]> results = query.list();
		  ArrayList blockresinner = new ArrayList();
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
		 for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();
			  ArrayList blockres = new ArrayList();
			  Map<String, String> assoc = new HashMap<String, String>();
			  blockres.add(row[0].toString());
			  blockres.add(row[1].toString());
			  blockres.add(row[2].toString());
			  blockres.add(row[3].toString());
			  blockres.add(row[4].toString());
			  blockres.add(row[5].toString());
			  blockres.add(row[6].toString());
			  blockres.add(row[7].toString());
			  blockres.add(row[8].toString());
			  blockres.add(row[9].toString());
			  blockres.add(row[10].toString());
			  blockres.add(row[11].toString());
			  blockres.add(row[12].toString());
			  blockres.add(row[13].toString());
			  blockres.add(row[14].toString());
			  blockres.add(row[15].toString());
			  blockres.add(row[16].toString());
			  blockres.add(row[17].toString());
			  blockres.add(row[18].toString());
			  blockres.add(row[19].toString());
			  blockres.add(row[20].toString());
			  blockres.add(row[21].toString());
			  blockres.add(row[22].toString());
			  blockres.add(row[23].toString());
			  blockresinner.add(blockres);
			}
		    return blockresinner;
		      
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  
			  return scheduledData;
		  }
		  }
	  
}
