package com.engage.dao;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

	  public long save(Pathway patient) {
	    long id=(long) getSession().save(patient);
	    return id;
	  }
	  
	  public void delete(Pathway patient) {
	    getSession().delete(patient);
	    return;
	  }
	  
	  @SuppressWarnings("unchecked")
	  public List<Pathway> getAll(ArrayList<Integer> ids) {
		  List<Pathway> praticenames=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Pathway.class);
			  crit.add(Restrictions.in("id", ids));
			  praticenames=crit.list();
			  
			  return praticenames;
		  }catch(Exception ex)
		  {
			  return praticenames;
		  }
	  }
	  
//	  @SuppressWarnings("unchecked")
	public List<Pathway> verifyId(long id) {
		  List<Pathway> patient=null;
		  try
		  {
			  Criteria crit = getSession().createCriteria(Pathway.class);
			  crit.add(Restrictions.eq("id",id));
			  patient= crit.list();
			  
			  return patient;
		  }catch(Exception ex)
		  {
			  return patient;
		  }
	    
	  }
//Get Pathway by ID
	  public Pathway getById(long id) {
		  
		  return (Pathway) getSession().get(Pathway.class, id);
		 
	  }
	
	  public void update(Pathway user) {
	    getSession().update(user);
	    return;
	  }

}
