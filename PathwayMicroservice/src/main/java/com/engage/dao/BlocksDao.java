package com.engage.dao;


import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import com.engage.model.Blocks;
import com.engage.model.Events;
import com.engage.model.ScheduleJson;
import com.engage.util.AdvancedEncryptionStandard;

@Repository
@Transactional
public class BlocksDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public long save(Blocks pathwayBlock) {
	    long id=(long) getSession().save(pathwayBlock);
	    return id;
	  }
	  
	  public void delete(Blocks pathwayBlock) {
	    getSession().delete(pathwayBlock);
	    return;
	  }
	 
	  
	  public <T>  boolean contains(final T[] array, final T key) {
		    return Arrays.asList(array).contains(key);
		}
	  
	  @SuppressWarnings("unchecked")
	  public List<Blocks> getAll(Long pathwayId) {
		  List<Blocks> pathwayBlock=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Blocks.class);
			  crit.add(Restrictions.eq("pathwayId", pathwayId));
//			  crit.add(Restrictions.eq("status","Y"));
			  
			  pathwayBlock=crit.list();
			  
			  return pathwayBlock;
		  }catch(Exception ex)
		  {
			  return pathwayBlock;
		  }
	  }
	  public Boolean getByBlockName(String blockName,Long eventId) {
		  Boolean pathway=false;
		  try
		  {
			  
			  Criteria crit = getSession().createCriteria(Events.class);
			  crit.add(Restrictions.eq("blockName",blockName));
			  crit.add(Restrictions.eq("eventId",eventId));
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
	public List<Blocks> verifyId(long id) {
		  List<Blocks> pathwayBlocks=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Blocks.class);
			  crit.add(Restrictions.eq("id",id));
			  pathwayBlocks= crit.list();
			  
			  return pathwayBlocks;
		  }catch(Exception ex)
		  {
			  return pathwayBlocks;
		  }
	    
	  }

	  public Blocks getById(long id) {
		  return (Blocks) getSession().get(Blocks.class, id);
		 
	  }
	  
	
	  public void update(Blocks pathwayBlock) {
	    getSession().update(pathwayBlock);
	    return;
	  }
	  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<ScheduleJson>  getScheduledData() {
		  List<ScheduleJson> scheduledData=new ArrayList<ScheduleJson>();
		  try{
				
		String sql = "Select a.pathway_id,a.event_id,a.block_name,a.block_pos_row,a.body_of_message from qc_pathway_block as a ,qc_pathway as b,qc_patient_pathway_info as c where a.pathway_id=b.id and a.block_pos_row=DATEDIFF(c.accepteddate,SUBDATE(NOW(),1)) and c.pathway_id=a.pathway_id and c.accept='Y' GROUP BY a.pathway_id,a.block_name,a.block_pos_col,a.subject_of_message";				
		  SQLQuery query=getSession().createSQLQuery(sql);
				  List<Object[]> results = query.list();
		  for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();
				scheduleJson.setPathwayId(Integer.parseInt(row[0].toString()));
				scheduleJson.setEventId(Integer.parseInt(row[1].toString()));
				scheduleJson.setBlockName(row[2].toString());
				scheduleJson.setMessage(row[4].toString());
				scheduledData.add(scheduleJson);
				
			}
		    return scheduledData;
		    
		    
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  
			  return scheduledData;
		  }
		  }
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<ScheduleJson>  getScheduledDataAppointment() {
		  List<ScheduleJson> scheduledData=new ArrayList<ScheduleJson>();
		  try{
				
		 String sql = "Select a.pathway_id,a.event_id,a.block_name,a.block_pos_row,a.body_of_message from qc_pathway_block as a ,qc_pathway as b,qc_patient_pathway_info as c where a.pathway_id=b.id and DATEDIFF(NOW(),c.accepteddate)<8 and c.pathway_id=a.pathway_id and c.accept='Y' and a.block_type='A' GROUP BY a.pathway_id,a.block_name,a.block_pos_col,a.subject_of_message";				
		  SQLQuery query=getSession().createSQLQuery(sql);
		  List<Object[]> results = query.list();
		  for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();
				scheduleJson.setPathwayId(Integer.parseInt(row[0].toString()));
				scheduleJson.setEventId(Integer.parseInt(row[1].toString()));
				scheduleJson.setBlockName(row[2].toString());
				scheduleJson.setMessage(row[4].toString());
				scheduledData.add(scheduleJson);
				
			}
		    return scheduledData;
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  return scheduledData;
		  }
		  }
public String checkforpatientpathway(Integer patienid, Integer pathwayId) {
		  try
		  {
	String sql = "Select pathwaydb.qc_patient_pathway_info.* from pathwaydb.qc_patient_pathway_info WHERE pathwaydb.qc_patient_pathway_info.pathway_id="+pathwayId+" and pathwaydb.qc_patient_pathway_info.patient_id="+patienid+" and pathwaydb.qc_patient_pathway_info.accept='Y'";				
			  SQLQuery query=getSession().createSQLQuery(sql);
			  List<Object[]> presults= query.list();
			  for(Object[] pw: presults){
			  return pw[4].toString();
			  }
			  return "No Data";
		  }
		  catch(Exception ex)
			 {
			return ex.getMessage(); 
			 }
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
 
 @SuppressWarnings({ "unchecked", "null" })
 public List<Object>  getPathwayFirstMessage(int pathwayid) {
	  List<Object> scheduledData=new ArrayList<Object>();
	  try{
			
	 String sql = "Select pathwaydb.qc_pathway_block.* from pathwaydb.qc_pathway_block WHERE pathwaydb.qc_pathway_block.pathway_id ="+pathwayid+" and pathwaydb.qc_pathway_block.block_name='Sign Up'";				
	  SQLQuery query=getSession().createSQLQuery(sql);
	  List<Object[]> results = query.list();
	  ArrayList blockresinner = new ArrayList();
	  Object[] obj = new Object[] {};
	  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
	  for(Object[] row : results){
		  ScheduleJson scheduleJson = new ScheduleJson();
		  ArrayList blockres = new ArrayList();
		  Map<String, String> assoc = new HashMap<String, String>();
		  blockresinner.add(row[12].toString());
		}
	    return blockresinner;
	  }catch(Exception ex)
	  {
		  System.out.println(ex.getMessage());
		  return scheduledData;
	  }
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
	  public List<Object>  getBlockcrondata() {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.message_send_at = CURDATE();";				
		  SQLQuery query=getSession().createSQLQuery(sql);
		  List<Object[]> results = query.list();
		  System.out.println(results.size());
		  ArrayList blockres = new ArrayList();
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
		  for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();
			  Map<String, String> assoc = new HashMap<String, String>();
			  String rid=row[0].toString();
			  assoc.put("id",rid);
			  String pthwayid=row[1].toString();
			  assoc.put("pathway_id",pthwayid);
			  String patientid=row[2].toString();
			  assoc.put("patient_id",patientid);
			  String blockname=row[3].toString();
			  assoc.put("block_name",blockname);
			  String blocktype=row[4].toString();
			  assoc.put("block_type",blocktype);
			  String blockposrow=row[5].toString();
			  assoc.put("block_pos_row",blockposrow);
			  String messagesendat=row[6].toString();
			  assoc.put("message_send_at",messagesendat);
			  String patient_accepted_date=row[7].toString();
			  assoc.put("patient_accepted_date",patient_accepted_date);
			  String block_pos_col=row[8].toString();
			  assoc.put("block_pos_col",block_pos_col);
			  String trigger_id=row[9].toString();
			  assoc.put("trigger_id",trigger_id);
			  String delivery_days_after_trigger=row[10].toString();
			  assoc.put("delivery_days_after_trigger",delivery_days_after_trigger);
			  String repeat_for_number_of_days=row[11].toString();
			  assoc.put("repeat_for_number_of_days",repeat_for_number_of_days);
			  String subject_of_message=row[12].toString();
			  assoc.put("subject_of_message",subject_of_message);
			  String body_of_message=row[13].toString();
			  assoc.put("body_of_message",body_of_message);
			  String status=row[14].toString();
			  assoc.put("status",status);
			  String created_date=row[15].toString();
			  assoc.put("created_date",created_date);
			  String event_id=row[16].toString();
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

	  //Patient Parent Blocks Information
	  
	  public int updatePatientappointmentblock(long id,long pid,long brow,long bcol,String msentat,String aptdate) {
		 
			  String sql = "UPDATE pathwaydb.qc_pathway_patient_blocks SET block_pos_row=:block_pos_row,block_pos_col=:block_pos_col,message_send_at=:message_send_at,block_appointment_date=:block_appointment_date WHERE id=:id";
			  SQLQuery query=getSession().createSQLQuery(sql);
				  query.setLong("block_pos_row", brow);
				  query.setLong("block_pos_col", bcol);
				  query.setString("message_send_at", msentat);
				  query.setString("block_appointment_date", aptdate);
				  
				    query.setLong("id", id);
			        int statuscode = query.executeUpdate();
			  return statuscode;
			  }
	  
		 
	  //Delete Patient Child Block
	  public int deletePatientChildBlock(long id) {
			  String hql = "delete from pathwaydb.qc_pathway_patient_blocks where id= :id";
			  SQLQuery query = getSession().createSQLQuery(hql);
			   query.setLong("id", id);
			  int statuscode = query.executeUpdate();
			 return statuscode;
			  }
		 
	  
	  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getChildBlockdata(long pbid) {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.block_parent_id="+pbid+"";				
		
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
			  long mtime=Long.parseLong(row[24].toString());
			  assoc.put("msenttime",mtime);
			  blockres.add(assoc);
			}
	    return blockres;
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  
			  return scheduledData;
		  }
		  
	  }
	  

	  //End Of patient Parent Blocks information
	  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getPatientpathway(int patientid,int pathwayid) {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.pathway_id="+pathwayid+" and pathwaydb.qc_pathway_patient_blocks.patient_id="+patientid+"";				
		
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
			  long mtime=Long.parseLong(row[24].toString());
			  assoc.put("msenttime",mtime);
			  blockres.add(assoc);
			  }
		    return blockres;
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  return scheduledData;
		  }
		  
	  }
	  
	  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getPatientpathwayByevent(int patientid,int pathwayid,int eventid) {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
		 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.pathway_id="+pathwayid+" and pathwaydb.qc_pathway_patient_blocks.patient_id="+patientid+" and pathwaydb.qc_pathway_patient_blocks.event_id="+eventid+"";				
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
	  
  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getPatientpathwayblocksByevent(int patientid,int pathwayid,int eventid) {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.pathway_id="+pathwayid+" and pathwaydb.qc_pathway_patient_blocks.patient_id="+patientid+" and pathwaydb.qc_pathway_patient_blocks.event_id="+eventid+"";				
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
			  assoc.put("pathwayId",pthwayid);
			  Integer ppatientid=Integer.parseInt(row[2].toString());
			  assoc.put("patientId",ppatientid);
			  Integer bblocid=Integer.parseInt(row[3].toString());
			  assoc.put("blockId",bblocid);
		  String bappointmentdate=row[4].toString();
			  assoc.put("blockAppointmentDate",bappointmentdate);
		  Integer bparentid=Integer.parseInt(row[5].toString());
			  assoc.put("blockAppointmentParent",bparentid);
		  String blockname=row[6].toString();
			  assoc.put("blockName",blockname);
			  String blocktype=row[7].toString();
			  assoc.put("blockType",blocktype);
			  Integer blockposrow=Integer.parseInt(row[8].toString());
			  assoc.put("blockPocRow",blockposrow);
			  String messagesendat=row[9].toString();
			  assoc.put("messageSendAt",messagesendat);
			  String messagestatus=row[10].toString();
			  assoc.put("messageStatus",messagestatus);
			  String patient_accepted_date=row[11].toString();
			  assoc.put("patientAcceptedDate",patient_accepted_date);
 Integer block_pos_col=Integer.parseInt(row[12].toString());
			  assoc.put("blockPocCol",block_pos_col);
  Integer trigger_id=Integer.parseInt(row[13].toString());
			  assoc.put("triggerId",trigger_id);
			  Integer delivery_days_after_trigger=Integer.parseInt(row[14].toString());
			  assoc.put("deliveryTrigger",delivery_days_after_trigger);
			  Integer repeat_for_number_of_days=Integer.parseInt(row[15].toString());
			  assoc.put("repeatOfdays",repeat_for_number_of_days);
			  String subject_of_message=row[16].toString();
			  assoc.put("subjectOfMessage",subject_of_message);
		  String body_of_message=row[17].toString();
			  assoc.put("bodyOfMessage",body_of_message);
		  String remindermessage=row[18].toString();
			  assoc.put("remainderOfMessage",remindermessage);
		  String fmessage=row[19].toString();
			  assoc.put("followupOfMessage",fmessage);
	  String status=row[20].toString();
			  assoc.put("status",status);
			  String created_date=row[21].toString();
			  assoc.put("created_date",created_date);
			  Integer event_id=Integer.parseInt(row[22].toString());
			  assoc.put("eventId",event_id);
			  String phisecured=row[23].toString();
			  assoc.put("phiSecured",phisecured);
			 blockres.add(assoc);
						}

		    return blockres;
		    
		    
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  
			  return scheduledData;
		  }
		  
	  }
	  
	  
	  //Getting the Events Information for pathway
	  

	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getpathwayeventsbyid(int pathwayid) {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway_event.* from pathwaydb.qc_pathway_event WHERE pathwaydb.qc_pathway_event.pathway_id="+pathwayid+"";				
		
		  SQLQuery query=getSession().createSQLQuery(sql);

		  List<Object[]> results = query.list();

		  ArrayList blockres = new ArrayList();
		  
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
		
		  
		 
		  for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();
			 
			 
			  Map<String, Object> assoc = new HashMap<String, Object>();
			  String eventname=row[2].toString();
			  assoc.put("eventname",eventname);
			  Integer erow=Integer.parseInt(row[3].toString());
			  assoc.put("erow",erow);
			  Integer ecol=Integer.parseInt(row[4].toString());
			  assoc.put("ecol",ecol);
			  blockres.add(assoc);
			}

		    return blockres;
		    
		    
		  }catch(Exception ex)
		  {
			  System.out.println(ex.getMessage());
			  
			  return scheduledData;
		  }
		  
	  }
	  


	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getpathwaysbyclinicianid(int cid) {
		  List<Object> scheduledData=new ArrayList<Object>();
		  try{
				
		 String sql = "Select pathwaydb.qc_pathway.* from pathwaydb.qc_pathway WHERE pathwaydb.qc_pathway.clinician_id="+cid+"";				
		
		  SQLQuery query=getSession().createSQLQuery(sql);
	  List<Object[]> results = query.list();
		  ArrayList blockres = new ArrayList();
		  Object[] obj = new Object[] {};
		  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
	  for(Object[] row : results){
			  ScheduleJson scheduleJson = new ScheduleJson();

			  Map<String, Object> assoc = new HashMap<String, Object>();
		  Integer pathwayid=Integer.parseInt(row[0].toString());
			  assoc.put("id",pathwayid);
			  Integer clinicianid=Integer.parseInt(row[1].toString());
			  assoc.put("clinicianid",clinicianid);
		  Integer teamid=Integer.parseInt(row[2].toString());
			  assoc.put("teamid",teamid);
			  String pathwayname=row[3].toString();
			  assoc.put("pathwayname",pathwayname);
			  String createddate=row[4].toString();
			  assoc.put("createddate",createddate);
			  String updateddate=row[4].toString();
			  assoc.put("updateddate",updateddate);
			  String status=row[4].toString();
			  assoc.put("status",status);
			  blockres.add(assoc);
	  
			  
			}

		    return blockres;
		    
		    
		  }catch(Exception ex)
		  {

			  
			  return scheduledData;
		  }
		  
	  }
	  

	  
	  //End of Pathway list by clinician
	  
	  
	  //Patient Block Pathway Block Message Information By ID
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
	  
	  

public String patientcron(String phone){
		  
		  /***
		   * Step 1. Get the Patient Phone 
		   * 
		   * Step 2: Using Patient Phone get the patient information
		   *  
		   * 
		   */
	try
	{
	
	
	String where="where pathwaydb.qc_patient_pathway_info.status='Y'";
	String sql="Select pathwaydb.qc_patient_pathway_info.* from pathwaydb.qc_patient_pathway_info "+where;
	SQLQuery query=getSession().createSQLQuery(sql);
		
			  List<Object[]> results= query.list();
			
			  for(Object[] user: results){
				  
			        Integer patientid = Integer.parseInt(user[1].toString());
			 		    
			      Integer pathwayid=Integer.parseInt(user[2].toString());
			      String acpdate=user[4].toString();
			       String pwhere="where pathwaydb.qc_pathway_patient_blocks.patient_id="+patientid+" and pathwaydb.qc_pathway_patient_blocks.pathway_id="+pathwayid+"";
			        String psql="Select count(*) from pathwaydb.qc_pathway_patient_blocks "+pwhere;
		 		  SQLQuery pquery=getSession().createSQLQuery(psql);
			  BigInteger count = (BigInteger)pquery.uniqueResult();
			  if (count.compareTo(BigInteger.ZERO) > 0) {
			        	System.out.println("Already exists Record");
			        }
			        else
			        {
			 
			        	//Get the pathway Information 
						String hql = "select pathwaydb.qc_pathway_block.* from pathwaydb.qc_pathway_block  WHERE pathwaydb.qc_pathway_block.pathway_id = "+pathwayid;
			        	SQLQuery pathquery=getSession().createSQLQuery(hql);
			        	List<Object[]> pathresults= pathquery.list();
		        	if(pathquery.list().size() > 0)
			        	{
		
   		  for(Object[] pw: pathresults){
			        			  Integer event_id=Integer.parseInt(pw[17].toString());
			  			    String ptwhere="where patientdb.qc_patient_pathway.patient_id="+patientid+" and patientdb.qc_patient_pathway.pathway_id="+pathwayid+" and patientdb.qc_patient_pathway.event_id="+event_id+"";
						        String ptsql="Select count(*) from patientdb.qc_patient_pathway "+ptwhere;
							  SQLQuery ptquery=getSession().createSQLQuery(ptsql);
							  BigInteger ptcount = (BigInteger)ptquery.uniqueResult();
							  if (ptcount.compareTo(BigInteger.ZERO) > 0) {
	        			  Integer blockid = Integer.parseInt(pw[0].toString());
		        Integer pwid = Integer.parseInt(pw[1].toString());
		  			        //block_name
			  			        String bname=(pw[2].toString());
			  			        System.out.println("Block name is "+bname);
			  			        //block_type
			  			        String btype=(pw[3].toString());
		  			        //PHI Secured
			  			      String phisecured=(pw[4].toString());
			  			      
			  			        //block appointment parent
			  			      Integer blockappointmentparent = Integer.parseInt(pw[5].toString());
			  			   
			  			        
			  			        //block_pos_row
			  			        Integer brow=Integer.parseInt(pw[6].toString());
			  			  
			  			      String dt = acpdate;  // Start date
					        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					        	Calendar c = Calendar.getInstance();
					        	c.setTime(sdf.parse(dt));
					        	c.add(Calendar.DATE, (brow-1));  // number of days to add
					        	
					        	dt = sdf.format(c.getTime());  // dt is now the new date
					        	//Later we need to modify it
					        	
					        	String appointmentdate=dt;
					        	String messeagesentstatus="scheduled";
					        	Long mtime=(long)0;
					        	if(bname.equals("Sign Up"))
					        	{
					        		messeagesentstatus="sent";
					        		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					        		  mtime=timestamp.getTime();
					        		
					        	}
					        	else
					        	{
					        		messeagesentstatus="scheduled";
					        	}
					        	
					        	
					        	
		
			  			        Integer bcol=Integer.parseInt(pw[7].toString());
			  			   
			  			      Integer triggerid=Integer.parseInt(pw[8].toString());
			  			    
			  			      Integer deliverydaysaftertrigger=Integer.parseInt(pw[9].toString());
			  			   
			  			      Integer repeatfornumberofdays=Integer.parseInt(pw[10].toString());
			  			   
			  			        
			  			      String subjectofmessage=(pw[11].toString());
			  			     
			  			      String bodyofmessage=(pw[12].toString());
			  			
			  			      String remaindermessage=(pw[13].toString());
			
			  			      String followupmessage=(pw[14].toString());
			  			        
			  			      String status=(pw[15].toString());
			  			  
	

String bisql = "INSERT INTO pathwaydb.qc_pathway_patient_blocks (pathway_id,patient_id,block_id,block_appointment_date,block_parent_id,block_name,block_type,block_pos_row,message_send_at,message_status,patient_accepted_date,block_pos_col,trigger_id,delivery_days_after_trigger,repeat_for_number_of_days,subject_of_message,body_of_message,remainder_of_message,followup_of_message,status,event_id,phi_secured,msenttime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


SQLQuery bnquery=getSession().createSQLQuery(bisql);

bnquery.setInteger(0, pathwayid);
bnquery.setInteger(1, patientid);
bnquery.setInteger(2, blockid);
bnquery.setString(3, appointmentdate);
bnquery.setInteger(4, blockappointmentparent);
bnquery.setString(5, bname);
bnquery.setString(6, btype);
bnquery.setInteger(7, brow);
bnquery.setString(8, dt);
bnquery.setString(9, messeagesentstatus);
bnquery.setString(10, acpdate);
bnquery.setInteger(11, bcol);
bnquery.setInteger(12, triggerid);
bnquery.setInteger(13, deliverydaysaftertrigger);
bnquery.setInteger(14, repeatfornumberofdays);
bnquery.setString(15, subjectofmessage);
bnquery.setString(16, bodyofmessage);
bnquery.setString(17, remaindermessage);
bnquery.setString(18, followupmessage);
bnquery.setString(19, "Y");


bnquery.setInteger(20, event_id);
bnquery.setString(21, phisecured);
bnquery.setLong(22, mtime);
			  int bnstatuscode=bnquery.executeUpdate();
				  		
								  }
			        		  }
			        		
			        		
			        		
			        		
			        		
			        	}
		        	
			        }
    }
		
		  return phone;
	}
	catch(Exception ex)
	  {
		  System.out.println(ex.getMessage());
		  
		return "Error";
	  }
	  }


//Update Patient QC Patient Block here

public int updatePatientblockmessagetxt(int pid,int bid,String mtype,String mtxt) {


	String sql="";
	 //Block Message Body of message
	if(mtype.equals("M"))
	{

	  sql = "UPDATE pathwaydb.qc_pathway_patient_blocks SET body_of_message=:body_of_message WHERE patient_id=:patient_id and block_id=:block_id";
	}
	//Reminder Message
	if(mtype.equals("R"))
	{
	
	  sql = "UPDATE pathwaydb.qc_pathway_patient_blocks SET remainder_of_message=:remainder_of_message WHERE patient_id=:patient_id and block_id=:block_id";
	}
	//Followup Message
	if(mtype.equals("F"))
	{
		
	  sql = "UPDATE pathwaydb.qc_pathway_patient_blocks SET followup_of_message=:followup_of_message WHERE patient_id=:patient_id and block_id=:block_id";
	}
		  SQLQuery query=getSession().createSQLQuery(sql);
		
		  
		  //Block Message Body of message
			if(mtype.equals(("M")))
			{
				  query.setParameter("body_of_message", mtxt);
	
			}
			//Reminder Message
			if(mtype.equals(("R")))
			{
		
				  query.setParameter("remainder_of_message", mtxt);
			}
			//Followup Message
			if(mtype.equals(("F")))
			{
				  query.setParameter("message_status", mtxt);
			}
		    query.setLong("patient_id", pid);
	        query.setLong("block_id", bid);
	        int statuscode = query.executeUpdate();

		  return statuscode;
	  }


//End of Update QC Patient Block here




//Insert Patient Pathway Block Information

public int insertpatientpathwayblocks(int pwid,
		int patientid,			        			  
		int blockid,
		String appointmentdate,
		int blockappointmentparent,
		String bname,
		String btype,
		int brow,
		String messagesendat,
		String messeagesentstatus,
		String patientaccepteddate,
		String phisecured,
		int bcol,
		int triggerid,
		int deliverydaysaftertrigger,
		int repeatfornumberofdays,
		String subjectofmessage,
		String bodyofmessage,
		String remaindermessage,
		String followupmessage,
		String status,
		int event_id,
		int msenttime) {


String bisql = "INSERT INTO pathwaydb.qc_pathway_patient_blocks (pathway_id,patient_id,block_id,block_appointment_date,block_parent_id,block_name,block_type,block_pos_row,message_send_at,message_status,patient_accepted_date,block_pos_col,trigger_id,delivery_days_after_trigger,repeat_for_number_of_days,subject_of_message,body_of_message,remainder_of_message,followup_of_message,status,event_id,phi_secured,msenttime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


SQLQuery bnquery=getSession().createSQLQuery(bisql);

bnquery.setInteger(0, pwid);
bnquery.setInteger(1, patientid);
bnquery.setInteger(2, blockid);
bnquery.setString(3, appointmentdate);
bnquery.setInteger(4, blockappointmentparent);
bnquery.setString(5, bname);
bnquery.setString(6, btype);
bnquery.setInteger(7, brow);
bnquery.setString(8, messagesendat);
bnquery.setString(9, messeagesentstatus);
bnquery.setString(10, patientaccepteddate);
bnquery.setInteger(11, bcol);
bnquery.setInteger(12, triggerid);
bnquery.setInteger(13, deliverydaysaftertrigger);
bnquery.setInteger(14, repeatfornumberofdays);
bnquery.setString(15, subjectofmessage);
bnquery.setString(16, bodyofmessage);
bnquery.setString(17, remaindermessage);
bnquery.setString(18, followupmessage);
bnquery.setString(19, "Y");


bnquery.setInteger(20, event_id);
bnquery.setString(21, phisecured);
bnquery.setLong(22, msenttime);
int bnstatuscode=bnquery.executeUpdate();
				  		
return bnstatuscode;
	  }

//End of patient Pathway Block Information




//Patient block information here

@SuppressWarnings({ "unchecked", "null" })
public List<Object>  getBlockinfoforpatient(int id) {
	  List<Object> scheduledData=new ArrayList<Object>();
	  try{
			
	 String sql = "Select pathwaydb.qc_pathway_patient_blocks.* from pathwaydb.qc_pathway_patient_blocks WHERE pathwaydb.qc_pathway_patient_blocks.block_id ="+id+"";				
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
		  
		  
		  
		  blockresinner.add(blockres);
		}
	    return blockresinner;
	    
	    
	  }catch(Exception ex)
	  {
		  System.out.println(ex.getMessage());
		  
		  return scheduledData;
	  }
	  }
//End of lakshmi data

//End of Patient block information
	  //end of lakshmi fun ends here

	private Date Date(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
