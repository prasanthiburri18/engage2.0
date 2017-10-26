package com.engage.dao;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.engage.model.Organization;

@Repository
@Transactional
public class OrganizationDao extends AbstractDao<Organization>{
  
	private static Logger LOGGER = LoggerFactory.getLogger(OrganizationDao.class);
	
  @Autowired
  private SessionFactory _sessionFactory;
  
 /* private Session getSession() {
    return _sessionFactory.getCurrentSession();
  }*/

  public Integer save(Organization organization) {
    Integer id=(Integer) getSession().save(organization);
    return id;
  }
  
  public Organization getById(Integer cid) {
	  Organization organization=null;
	  try
	  {
		  
		  Criteria crit = getSession().createCriteria(Organization.class);
		  crit.add(Restrictions.eq("id",cid));
		  organization=(Organization) crit.uniqueResult();
	    return organization;
	  }catch(Exception e)
	  {
		  LOGGER.info(e.getMessage());
		  return organization;
	  }
	 
  }
  
 
  
  
  public void update(Organization organization) {
	    getSession().update(organization);
	    return;
	  }
  
}