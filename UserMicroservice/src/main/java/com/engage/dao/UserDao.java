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