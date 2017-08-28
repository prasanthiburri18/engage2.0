package com.engage.model;

import java.io.Serializable;

public class ScheduleJson  {
	private int pathwayId;
	private int eventId;
	private String blockName="";
	private String message="";
	
	public Integer getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(Integer pathwayId) {
		this.pathwayId = pathwayId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ScheduleJson() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
