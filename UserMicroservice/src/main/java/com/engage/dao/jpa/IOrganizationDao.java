package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Organization;
/**
 * 
 * @author ai
 *
 */
@Repository
public interface IOrganizationDao extends JpaRepository<Organization, Integer>{
	
}
