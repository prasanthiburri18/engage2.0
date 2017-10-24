/**
 * 
 */
package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.Blocks;

/**
 * @author mindtech-labs
 *
 */
@Repository
public interface IBlocksDao extends JpaRepository<Blocks, Long> {

}
