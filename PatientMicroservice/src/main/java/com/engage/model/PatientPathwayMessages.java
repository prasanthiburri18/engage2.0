package com.engage.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.engage.util.AdvancedEncryptionStandard;
@Entity
@Table(name="qc_pathway_patient_message")
public class PatientPathwayMessages {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	 private long id;
	 @Column(name="team_id")
	 private long teamId;
	 @Column(name="patient_id")
	 private long patientId;
	 @Column(name="block_id")
	 private long blockId;
	 private long frequency;
	 @Column(name="trigger_id")
	 private long triggerId;
	 @Column(name="start_date")
	 private Date startDate;
	 @Column(name="end_date")
	 private Date endDate;
	 @Column(name="message_type")
	 private String messageType;
	 private String message;
	 private String subject;
	 private String phisecured;
	 @Column(name="resource_link")
	 private String resourceLink;
	 private String status;
	 @Column(name="created_date")
	 private Date createdDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public long getPatientId() {
		return patientId;
	}
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}
	public long getBlockId() {
		return blockId;
	}
	public void setBlockId(long blockId) {
		this.blockId = blockId;
	}
	public long getFrequency() {
		return frequency;
	}
	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}
	public long getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(long triggerId) {
		this.triggerId = triggerId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessage() {

		try {
			return AdvancedEncryptionStandard.decrypt(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return message;
		}
	}
	public void setMessage(String message) {
		try {
			this.message = AdvancedEncryptionStandard.encrypt(message);
		} catch (Exception e) {
			this.message=null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getSubject() {
	
		try {
			return AdvancedEncryptionStandard.decrypt(subject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return subject;
		}
	}
	public void setSubject(String subject) {
		try {
			this.subject = AdvancedEncryptionStandard.encrypt(subject);
		} catch (Exception e) {
			this.message=null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getPhisecured() {
		return phisecured;
	}
	public void setPhisecured(String phisecured) {
		this.phisecured = phisecured;
	}
	public String getResourceLink() {
		return resourceLink;
	}
	public void setResourceLink(String resourceLink) {
		this.resourceLink = resourceLink;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public PatientPathwayMessages() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PatientPathwayMessages(long teamId, long patientId, long blockId, long frequency, long triggerId,
			Date startDate, Date endDate, String messageType, String message, String subject, String phisecured,
			String resourceLink, String status, Date createdDate) {
		super();
		this.teamId = teamId;
		this.patientId = patientId;
		this.blockId = blockId;
		this.frequency = frequency;
		this.triggerId = triggerId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.messageType = messageType;
		this.message = message;
		this.subject = subject;
		this.phisecured = phisecured;
		this.resourceLink = resourceLink;
		this.status = status;
		this.createdDate = createdDate;
	}
	 
	 
	 
	
}
