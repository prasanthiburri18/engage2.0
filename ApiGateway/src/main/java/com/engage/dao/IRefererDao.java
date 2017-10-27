/**
 * 
 */
package com.engage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Referer;

/**
 * @author ai
 *
 */
@Repository
public interface IRefererDao extends JpaRepository<Referer, Long> {

}
