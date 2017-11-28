/**
 * 
 */
package com.engage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Referer;

/**
 * <p>Spring Data JPA respository for {@link Referer} entity </p>
 * @author ai
 *
 */
@Repository
public interface IRefererDao extends JpaRepository<Referer, Long> {

}
