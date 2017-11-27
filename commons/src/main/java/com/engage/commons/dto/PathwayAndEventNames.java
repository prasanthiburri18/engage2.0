/**
 * 
 */
package com.engage.commons.dto;

import java.sql.Date;

/**
 * @author mindtech-labs
 *
 */
public class PathwayAndEventNames {

	private Integer pathwayId;
	private String pathwayName;
	private Integer eventId;
	private Integer patientId;
	private String eventName;
	private Date acceptedDate;
	private Integer orgId;

	public Integer getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(Integer pathwayId) {
		this.pathwayId = pathwayId;
	}
	public String getPathwayName() {
		return pathwayName;
	}
	public void setPathwayName(String pathwayName) {
		this.pathwayName = pathwayName;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Date getAcceptedDate() {
		return acceptedDate;
	}
	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}


}
