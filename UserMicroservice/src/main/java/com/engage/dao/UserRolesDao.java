package com.engage.dao;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.engage.model.UserRoles;

@Repository
@Transactional
public class UserRolesDao {
  
  @Autowired
  private SessionFactory _sessionFactory;
  
  private Session getSession() {
    return _sessionFactory.getCurrentSession();
  }

  public void save(UserRoles roles) {
    getSession().save(roles);
    return;
  }
  
  public void delete(UserRoles roles) {
    getSession().delete(roles);
    return;
  }
  
  @SuppressWarnings("unchecked")
  public List<UserRoles> getAll() {
    return getSession().createQuery("from UserRoles").list();
  }
  
  public UserRoles getByuserId(BigInteger userId) {
    return (UserRoles) getSession().createQuery(
        "from UserRoles where userId = :userId")
        .setParameter("userId", userId)
        .uniqueResult();
  }

  public UserRoles getById(long id) {
    return (UserRoles) getSession().load(UserRoles.class, id);
  }

  public void update(UserRoles roles) {
    getSession().update(roles);
    return;
  }

}