package com.engage.model;

import java.sql.Timestamp;
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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Length.List;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "qc_pathway")
public class Pathway {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull(message = "Pathway name cannot be empty.")
	@NotBlank(message = "Pathway name cannot be empty.")
	@Length(max = 30, message = "Pathway name exceeds {max} characters.")
	@Pattern(regexp = "^[a-zA-z0-9\\s]*$", message = "Only alphanumeric characters are allowed.")
	@Column(name = "pathway_name")
	private String pathwayName;
	
	@Min(value = 1, message = "Invalid Organization Id")
	// Since db has bigint, it is safe to user Integer.MAX_VALUE
	@Max(value = Integer.MAX_VALUE, message = "Invalid Organization Id")
	@Column(name = "org_id")
	private long orgId;
	@Column(name = "team_id")
	private long teamId;
	
	
	@Pattern(regexp="^[YNyn]*$", message="Invalid Status")
	@Length( max = 1, message = "Invalid Status")
	private String status;
	@Column(name = "created_date")
	private Timestamp createDate;
	@Column(name = "updated_date")
	private Timestamp updateDate;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "pathway_id")
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

	public Pathway(String pathwayName, long orgId, long teamId, String status,
			Timestamp createDate, Timestamp updateDate, Set<Events> events) {
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
