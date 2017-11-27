package com.engage.commons.dto;

import java.sql.Timestamp;
import java.util.Set;

public class PathwayDto {
	private long id;

	private String pathwayName;
	
	private long orgId;
	private long teamId;
	
	
	private String status;
	private Timestamp createDate;
	private Timestamp updateDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPathwayName() {
		return pathwayName;
	}

	public void setPathwayName(String pathwayName) {
		this.pathwayName = pathwayName;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Set<EventsDto> getEvents() {
		return events;
	}

	public void setEvents(Set<EventsDto> events) {
		this.events = events;
	}

	private Set<EventsDto> events;

}
