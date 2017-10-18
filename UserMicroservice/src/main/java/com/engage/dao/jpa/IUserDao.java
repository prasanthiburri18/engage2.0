package com.engage.dao.jpa;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.engage.model.User;

public interface IUserDao extends JpaRepository<User, BigInteger>{

	public List<User> getUserByOrgid(int orgid);
	
	@Query("select distinct practiceName from User")
	public List<String> getAllPracticeNames();
	
	public User findUserByEmailIgnoreCase(String string);
}
