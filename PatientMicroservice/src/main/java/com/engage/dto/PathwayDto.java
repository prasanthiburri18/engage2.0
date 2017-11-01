package com.engage.dto;

import java.sql.Timestamp;
import java.util.Set;


public class PathwayDto {

	private Long id;

	private String pathwayName;
	
	private Long orgId;
	private Long teamId;
	
	
	
	private String status;
	private Timestamp createDate;
	private Timestamp updateDate;

	private Set<String> events;

	public Set<String> getEvents() {
		return events;
	}

	
}
