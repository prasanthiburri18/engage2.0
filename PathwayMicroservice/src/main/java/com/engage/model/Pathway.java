package com.engage.model;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.engage.util.AdvancedEncryptionStandard;

@Entity
@Table(name="qc_pathway")
public class Pathway {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	 @Column(name="pathway_name")
	  private String pathwayName;
	  @Column(name="org_id")
	  private long orgId;
	  @Column(name="team_id")
	  private long teamId;
	  private String status;
	  @Column(name="created_date")
	  private Timestamp createDate;
	  @Column(name="updated_date")
	  private Timestamp updateDate;
	  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	 @JoinColumn(name="pathway_id")
	  @Fetch(FetchMode.SUBSELECT)
	 private Set<Events> events;   
	public Set<Events> getEvents() {
		return events;
	}
	public void setEvents(Set<Events> events) {
		this.events = events;
	}
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
	public long getorgId() {
		return orgId;
	}
	public void setorgId(long orgId) {
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
	public Pathway() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pathway(String pathwayName, long orgId, long teamId, String status, Timestamp createDate,
			Timestamp updateDate, Set<Events> events) {
		super();
		this.pathwayName = pathwayName;
		this.orgId = orgId;
		this.teamId = teamId;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.events = events;
	}
	
	
	  

}
