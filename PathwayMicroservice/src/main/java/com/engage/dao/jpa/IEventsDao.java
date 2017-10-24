/**
 * 
 */
package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Events;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IEventsDao extends JpaRepository<Events, Long> {

}
