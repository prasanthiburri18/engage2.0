package com.engage.commons.dto;

import java.sql.Date;
import java.util.Set;
/**
 * Data Transfer Object of Events Entity of Pathway Microservice.
 * @author mindtechlabs
 *
 */
public class EventsDto {
	
	private long id;
		private String eventName;

	private long pathwayId;
	private long eventPocRow;
	private long eventPocCol;
	
	
	private String status;
	private Date createDate;
	private Set<BlocksDto> blocks;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public long getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(long pathwayId) {
		this.pathwayId = pathwayId;
	}
	public long getEventPocRow() {
		return eventPocRow;
	}
	public void setEventPocRow(long eventPocRow) {
		this.eventPocRow = eventPocRow;
	}
	public long getEventPocCol() {
		return eventPocCol;
	}
	public void setEventPocCol(long eventPocCol) {
		this.eventPocCol = eventPocCol;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Set<BlocksDto> getBlocks() {
		return blocks;
	}
	public void setBlocks(Set<BlocksDto> blocks) {
		this.blocks = blocks;
	}

}
