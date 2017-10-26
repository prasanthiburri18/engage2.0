package com.engage.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * {@link AbstractDao} is used as super class for dao objects. 
 * Implemented using Spring-Hibernate.
 * Generics are used to leverage inheritance
 * @author mindtech-labs
 *
 * @param <T>
 */
@Repository
public abstract class AbstractDao<T> {
	/**
	 * Inclass logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);

	/**
	 * Wiring Hibernate {@link SessionFactory}
	 */
	@Autowired
	private SessionFactory _sessionFactory;

	protected Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	
	/**
	 * Update the object passed
	 * @param t
	 */
	public void update(T t) {
		LOGGER.info("Updating " + t.getClass().toString());
		getSession().update(t);

	}

	public void delete(T t) {
		LOGGER.info("Deleting " + t.getClass().toString());
		getSession().delete(t);

	}
}
