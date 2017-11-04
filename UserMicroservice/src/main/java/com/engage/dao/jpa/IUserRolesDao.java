package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.UserRoles;
/**
 * 
 * @author ai
 *
 */
@Repository
public interface IUserRolesDao extends JpaRepository<UserRoles, Integer>{
	
}
