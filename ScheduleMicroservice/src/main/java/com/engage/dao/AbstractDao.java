package com.engage.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public abstract class AbstractDao {
	@Autowired
	  private SessionFactory _sessionFactory;
	
	  
	  protected Session getSession() {
	    return _sessionFactory.getCurrentSession();
	  }

}
