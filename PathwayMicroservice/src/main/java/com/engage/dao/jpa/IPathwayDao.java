/**
 * 
 */
package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Pathway;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IPathwayDao extends JpaRepository<Pathway, Long> {

}
