package com.engage.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;





import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.engage.util.AdvancedEncryptionStandard;

@Entity
@Table(name="qc_patient_pathway_info")
public class Pathwayaccept {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	 @Column(name="patient_id")
	  private long patientId;
	  @Column(name="pathway_id")
	  private long pathwayId;
	  @Column(name="accept")
	  private String accept;
	  @Column(name="status")
	  private String status;
	  @Column(name="accepteddate")
	  private Timestamp accepteddate;

	  public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getPatientId() {
		return patientId;
	}
	public void setPatientId(long patientId) {
		System.out.println("psdas");
		System.out.print(pathwayId);
		this.patientId = patientId;
	}
	public long getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(long pathwayId) {
		System.out.println("das");
		System.out.print(pathwayId);
		this.pathwayId = pathwayId;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		System.out.println("cdas");
		this.status = status;
	}
	
	
	public Timestamp getacceptedDate() {
		return accepteddate;
	}
	public void setacceptedDate(Timestamp accepteddate) {
				
		this.accepteddate = accepteddate;
	}
	
	public String getAccept() {
		return accept;
	}
	public void setAccept(String accept) {
		this.accept = accept;
	}
	
	public Pathwayaccept() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pathwayaccept(long patientId, long pathwayId, String accept, String status, Timestamp accepteddate) {
		super();
		this.patientId = patientId;
		this.pathwayId = pathwayId;
		this.status = status;
		
		this.accepteddate =accepteddate;
		this.accept = accept;
	}
	
}
