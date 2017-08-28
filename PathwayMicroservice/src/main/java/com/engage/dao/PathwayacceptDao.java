package com.engage.dao;


import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.engage.model.Pathwayaccept;
import com.engage.model.Events;
import com.engage.model.ScheduleJson;
import com.engage.util.AdvancedEncryptionStandard;

@Repository
@Transactional
public class PathwayacceptDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  private Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

	  public long save(Pathwayaccept pathwayaccept) {
		 
	    long id=(long) getSession().save(pathwayaccept);
	    return id;
	  }
	  
		 
	  

	  
	 

}
