package com.engage.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engage.model.PathwayEvents;

@Repository
public interface IPathwayEventsDao extends JpaRepository<PathwayEvents, Long> {

}
