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
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.engage.model.ScheduleJson;
import com.engage.model.User;
import com.engage.util.AdvancedEncryptionStandard;

@Repository
@Transactional
public class UserDao {
  
  @Autowired
  private SessionFactory _sessionFactory;
  
  private Session getSession() {
    return _sessionFactory.getCurrentSession();
  }

  public BigInteger save(User user) {
    BigInteger id=(BigInteger) getSession().save(user);
    return id;
  }
  
  public void delete(User user) {
    getSession().delete(user);
    return;
  }
  
  @SuppressWarnings("unchecked")
  public List<User> getAll() {
    return getSession().createQuery("from User").list();
  }
  @SuppressWarnings("unchecked")
public List<String> getallPraticeNames() {
	  List<String> praticenames=null;
	  try
	  {
		  Criteria crit = getSession().createCriteria(User.class);
		  crit.setProjection(Projections.distinct(Projections.property("practiceName")));
		  praticenames=crit.list();
		  
		  return praticenames;
	  }catch(Exception ex)
	  {
		  return praticenames;
	  }
	  
  }
  @SuppressWarnings("unchecked")
public List<User> getByEmail(String email) {
	  List<User> user=null;
	  try
	  {
		  Criteria crit = getSession().createCriteria(User.class);
		  crit.add(Restrictions.eq("email",email));
		  user= crit.list();
		  
		  return user;
	  }catch(Exception ex)
	  {
		  return user;
	  }
    
  }

    @SuppressWarnings("unchecked")
  
public List<User> getByOrgids(long orgid) {
	  List<User> user=null;
	  try
	  {

		  
		  
		  String sql = "Select userdb.dt_users.* from userdb.dt_users WHERE userdb.dt_users.orgid="+orgid+"";				
			
		  SQLQuery query = getSession().createSQLQuery(sql);
		  List<Object[]> results = query.list();
		  ArrayList blockres = new ArrayList();
		  
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
		
		  for(Object[] row : results){
		  Map<String, String> assoc = new HashMap<String, String>();
		  
		  String rid=row[0].toString();
		  assoc.put("id",rid);
		  String email=row[1].toString();
		  assoc.put("email", email);
		  String ph=row[3].toString();
		  assoc.put("phone", ph);
		  String fullname=row[5].toString();
		  assoc.put("fullname", fullname);
		  String role=row[8].toString();
		  assoc.put("role", role);
		  blockres.add(assoc);
		  
		  }
		  
	  return blockres;
	  }catch(Exception ex)
	  {
		  return user;
	  }
    
  }

public ArrayList patientidbyphone(String  ph) {
	  try
	  {

		  String phone=AdvancedEncryptionStandard.encrypt(ph);
		  phone=phone.trim();
		  String sql = "Select patientdb.qc_patient.* from patientdb.qc_patient";
	  SQLQuery query = getSession().createSQLQuery(sql);
		  List<Object[]> results = query.list();
		  ArrayList blockres = new ArrayList();
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
		  for(Object[] row : results){
		  Map<String, String> assoc = new HashMap<String, String>();
		  
		  String rid=row[0].toString();
		  	  
		  String cp=row[6].toString();
		  if(AdvancedEncryptionStandard.decrypt(cp).equals(ph))
		  {
			  blockres.add(rid); 
		  }

		  }
		  
	  return blockres;
	  }catch(Exception ex)
	  {
		  return null;
	  }
    
  }
public int verifyPatientInfo(long patientid) {
	  String sql = "SELECT  pathwaydb.qc_patient_pathway_info.patient_id FROM pathwaydb.qc_patient_pathway_info where pathwaydb.qc_patient_pathway_info.patient_id="+patientid+"";
		SQLQuery query=getSession().createSQLQuery(sql);
		int statuscode= (int) query.list().size();
	  return statuscode;
	  }
public int verifyPatientInfobydob(String dob) {
	  String sql = "SELECT  patientdb.qc_patient.dob FROM patientdb.qc_patient where patientdb.qc_patient.dob='"+dob+"'";
	  SQLQuery query=getSession().createSQLQuery(sql);
	  int statuscode= (int) query.list().size();
return statuscode;
	  }
@SuppressWarnings({ "unchecked", "null" })
public List<Object>  getPatientpathwayblockById(int id) {
	  List<Object> scheduledData=new ArrayList<Object>();
	  try{
	String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.id="+id+"";				
	SQLQuery query=getSession().createSQLQuery(sql);
	List<Object[]> results = query.list();
	  ArrayList blockres = new ArrayList();
	  Object[] obj = new Object[] {};
	  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
	  for(Object[] row : results){
		  ScheduleJson scheduleJson = new ScheduleJson();
		  Map<String, Object> assoc = new HashMap<String, Object>();
		  Integer rid=Integer.parseInt(row[0].toString());
		  assoc.put("id",rid);
		  Integer pthwayid=Integer.parseInt(row[1].toString());
		  assoc.put("pathway_id",pthwayid);
		  Integer ppatientid=Integer.parseInt(row[2].toString());
		  assoc.put("patient_id",ppatientid);
		  Integer bblocid=Integer.parseInt(row[3].toString());
		  assoc.put("block_id",bblocid);
		  String bappointmentdate=row[4].toString();
		  assoc.put("block_appointment_date",bappointmentdate);
		  Integer bparentid=Integer.parseInt(row[5].toString());
		  assoc.put("block_parent_id",bparentid);
		  String blockname=row[6].toString();
		  assoc.put("block_name",blockname);
		  String blocktype=row[7].toString();
		  assoc.put("block_type",blocktype);
		  Integer blockposrow=Integer.parseInt(row[8].toString());
		  assoc.put("block_pos_row",blockposrow);
		  String messagesendat=row[9].toString();
		  assoc.put("message_send_at",messagesendat);
		  String messagestatus=row[10].toString();
		  assoc.put("message_status",messagestatus);
		  String patient_accepted_date=row[11].toString();
		  assoc.put("patient_accepted_date",patient_accepted_date);
		  Integer block_pos_col=Integer.parseInt(row[12].toString());
		  assoc.put("block_pos_col",block_pos_col);
		  Integer trigger_id=Integer.parseInt(row[13].toString());
		  assoc.put("trigger_id",trigger_id);
		  Integer delivery_days_after_trigger=Integer.parseInt(row[14].toString());
		  assoc.put("delivery_days_after_trigger",delivery_days_after_trigger);
		  Integer repeat_for_number_of_days=Integer.parseInt(row[15].toString());
		  assoc.put("repeat_for_number_of_days",repeat_for_number_of_days);
		  String subject_of_message=row[16].toString();
		  assoc.put("subject_of_message",subject_of_message);
		  String body_of_message=row[17].toString();
		  assoc.put("body_of_message",body_of_message);
		  String remindermessage=row[18].toString();
		  assoc.put("remainder_of_message",remindermessage);
		  String fmessage=row[19].toString();
		  assoc.put("followup_of_message",fmessage);
		  String status=row[20].toString();
		  assoc.put("status",status);
		  String created_date=row[21].toString();
		  assoc.put("created_date",created_date);
		  Integer event_id=Integer.parseInt(row[22].toString());
		  assoc.put("event_id",event_id);
		  blockres.add(assoc);
		  
		}

	    return blockres;
	    
	    
	  }catch(Exception ex)
	  {
		  System.out.println(ex.getMessage());
		  
		  return scheduledData;
	  }
	  
}

  
  public User getById(String email) {
	  User user=null;
	  try
	  {
	  Criteria crit = getSession().createCriteria(User.class);
		  crit.add(Restrictions.eq("email",email));
		   user=(User) crit.uniqueResult();
	    return user;
	  }catch(Exception e)
	  {
		  System.out.println(e.getMessage());
		  return user;
	  }
	 
  }
  public User getUserById(int id) {
	  User user=null;
	  try
	  {
		  
		  Criteria crit = getSession().createCriteria(User.class);
		  crit.add(Restrictions.eq("id",id));
		   user=(User) crit.uniqueResult();
	    return user;
	  }catch(Exception e)
	  {
		  System.out.println(e.getMessage());
		  return user;
	  }
	 
  }
  public User getByUserName(String user_name,String password) {
	  return (User) getSession().createQuery(
		        "from User where email = :email and password=:password")
		        .setParameter("email", user_name)
		        .setParameter("password", password)
		        .uniqueResult();
	  }
  public User getByUserByUid(BigInteger id) {
	  return (User) getSession().createQuery(
		        "from User where id = :id")
		        .setParameter("id", id)
		        .uniqueResult();
	  }

  public void update(User user) {
    getSession().update(user);
    return;
  }

}