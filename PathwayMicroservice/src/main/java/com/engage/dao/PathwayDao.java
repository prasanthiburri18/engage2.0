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
import com.engage.model.Pathway;


@Repository
@Transactional
public class PathwayDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public long save(Pathway pathway) {
		  
	    long id=(long) getSession().save(pathway);
	    return id;
	  }
	  
	  public void delete(Pathway pathway) {
	    getSession().delete(pathway);
	    return;
	  }
	  public Boolean getByPathwayName(String pathwayName,long orgId) {
		  Boolean pathway=false;
		  try
		  {
			  
			  Criteria crit = getSession().createCriteria(Pathway.class);
			  crit.add(Restrictions.eq("pathwayName",pathwayName));
			  crit.add(Restrictions.eq("orgId",orgId));
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
	  @SuppressWarnings("unchecked")
	  public List<Pathway> getAll(long ids) {
		  List<Pathway> pathways=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Pathway.class);
			  crit.add(Restrictions.eq("orgId", ids));
//			  crit.add(Restrictions.eq("status", "Y"));
			  pathways=crit.list();
			  
			  return pathways;
		  }catch(Exception ex)
		  {
			  return pathways;
		  }
	  }
	  
//	  @SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	public List<Pathway> verifyId(long id) {
		  List<Pathway> pathways=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Pathway.class);
			  crit.add(Restrictions.eq("id",id));
			  pathways= crit.list();
			  
			  return pathways;
		  }catch(Exception ex)
		  {
			  return pathways;
		  }
	    
	  }


	  public Pathway getById(long id) {
		  
		  return (Pathway) getSession().get(Pathway.class, id);
		 
	  }
	
	  public void update(Pathway pathway) {
	    getSession().update(pathway);
	    return;
	  }
	  
	  //Update Pathway Name
	  public int updatepathwayname(long id,String pathwayname,String updatetime) {
			 System.out.println("Update method called");
			 
			  String sql = "UPDATE pathwaydb.qc_pathway SET Pathway_name=:Pathway_name,updated_date=:updated_date WHERE id=:id";
				
				  SQLQuery query=getSession().createSQLQuery(sql);
				 
				  query.setString("Pathway_name", pathwayname);
				  query.setString("updated_date", updatetime);
				  
				  
				  
				    query.setLong("id", id);
			        int statuscode = query.executeUpdate();
			
				    return statuscode;
			  }
	  
	  //Get the Organization name by pathwayId
	  
	  @SuppressWarnings({ "unchecked", "null" })
	  public List<Object>  getorgnameforpathwayid(int pathwayid) {
	 	  List<Object> output=new ArrayList<Object>();
	 	  try{
	 			
	 	 String sql = "Select pathwaydb.qc_pathway.* from pathwaydb.qc_pathway WHERE pathwaydb.qc_pathway.id ="+pathwayid;			
	 	//String sql="select * from qc_pathway_block";		
	 	  SQLQuery query=getSession().createSQLQuery(sql);
	 	
	 	  List<Object[]> results = query.list();

	 	  ArrayList blockresinner = new ArrayList();
	 	  
	 	  Object[] obj = new Object[] {};
	 	  ArrayList<Object> newObj = new ArrayList<Object>(Arrays.asList(obj));
	 	
	 	  
	 	 
	 	  for(Object[] row : results){
	 		  
	 		  ArrayList blockres = new ArrayList();
	 		  Map<String, String> assoc = new HashMap<String, String>();
	 		  blockresinner.add(row[1].toString());
	 		}
	 	    return blockresinner;
	 	    
	 	    
	 	  }catch(Exception ex)
	 	  {
	 		  System.out.println(ex.getMessage());
	 		  
	 		  return output;
	 	  }
	 	  }

	  //End of getting the OrganizationId by Pathway Name
	  

}
