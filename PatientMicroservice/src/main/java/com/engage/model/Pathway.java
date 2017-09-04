package com.engage.model;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Length.List;

import com.engage.util.AdvancedEncryptionStandard;

@Entity
@Table(name="qc_pathway")
public class Pathway {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	 

		@NotNull(message = "Pathway name cannot be empty.")
		@NotBlank(message = "Pathway name cannot be empty.")
				@Length(max = 30, message = "Pathway name exceeds {max} characters.")
		@Pattern(regexp = "^[a-zA-z0-9\\s]*$", message = "Only alphanumeric characters are allowed.")
	 @Column(name="pathway_name")
	  private String pathwayName;
		
		@Min(value=1, message="Invalid Clinician Id")
		@Max(value=Integer.MAX_VALUE, message="Invalid Clinician Id")
	  @Column(name="clinician_id")
	  private long clinicianId;
	 
		@Column(name="team_id")
	  private long teamId;
	  private String status;
	  @Column(name="created_date")
	  private Date createDate;
	  @Column(name="updated_date")
	  private Date updateDate;
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
	public long getClinicianId() {
		return clinicianId;
	}
	public void setClinicianId(long clinicianId) {
		this.clinicianId = clinicianId;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Pathway() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pathway(String pathwayName, long clinicianId, long teamId, String status, Date createDate, Date updateDate) {
		super();
		this.pathwayName = pathwayName;
		this.clinicianId = clinicianId;
		this.teamId = teamId;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
	  
	
	  

}
