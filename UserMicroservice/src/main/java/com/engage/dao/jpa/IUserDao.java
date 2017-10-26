package com.engage.dao.jpa;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engage.model.User;
/**
 * 
 * @author ai
 *
 */
@Repository
public interface IUserDao extends JpaRepository<User, BigInteger>{
	/**
	 * 
	 * @param orgid
	 * @return
	 */
	public List<User> getUserByOrgid(Integer orgid);
	
	/**
	 * 
	 * @return
	 */
	@Query("select distinct practiceName from User")
	public List<String> getAllPracticeNames();
	/**
	 * 
	 * @param string
	 * @return
	 */
	public User findUserByEmailIgnoreCase(String string);
}
