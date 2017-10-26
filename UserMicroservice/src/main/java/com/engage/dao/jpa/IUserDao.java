package com.engage.dao.jpa;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engage.model.User;

@Repository
public interface IUserDao extends JpaRepository<User, BigInteger>{

	public List<User> getUserByOrgid(Long orgid);
	
	@Query("select distinct practiceName from User")
	public List<String> getAllPracticeNames();
	
	public User findUserByEmailIgnoreCase(String string);
}
