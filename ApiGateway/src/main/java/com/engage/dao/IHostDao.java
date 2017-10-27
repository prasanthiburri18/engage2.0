/**
 * 
 */
package com.engage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Host;

/**
 * @author ai
 *
 */
@Repository
public interface IHostDao extends JpaRepository<Host, Long> {

}
