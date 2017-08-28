package com.engage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="qc_patient_pathway")
public class PatientPathway {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	 private long id;
	 @Column(name="patient_id")
	 private long patientid;
	 @Column(name="pathway_id")
	 private long pathwayId;
	 @Column(name="event_id")
	 private long eventId;
	 @Column(name="team_id")
	 private long teamId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPatientid() {
		return patientid;
	}
	public void setPatientid(long patientid) {
		this.patientid = patientid;
	}
	public long getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(long pathwayId) {
		this.pathwayId = pathwayId;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public PatientPathway() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PatientPathway(long patientid, long pathwayId, long eventId, long teamId) {
		super();
		this.patientid = patientid;
		this.pathwayId = pathwayId;
		this.eventId = eventId;
		this.teamId = teamId;
	}
	
	 
}
