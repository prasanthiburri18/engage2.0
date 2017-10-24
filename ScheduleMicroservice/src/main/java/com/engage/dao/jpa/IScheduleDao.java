/**
 * 
 */
package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.ScheduledQueue;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IScheduleDao extends JpaRepository<ScheduledQueue, Long> {

}
