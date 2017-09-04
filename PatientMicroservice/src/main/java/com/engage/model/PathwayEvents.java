package com.engage.model;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Length.List;

import com.engage.util.AdvancedEncryptionStandard;

@Entity
@Table(name="qc_pathway_event")
public class PathwayEvents {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	 
		@NotNull(message = "Event name cannot be empty.")
		@NotBlank(message = "Event name cannot be empty.")
		@List({ @Length(min = 2, message = "Event name should be a minimum of {min} characters."),
				@Length(max = 30, message = "Event name exceeds {max} characters."), })
		@Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Only alphanumeric characters are allowed.")
	 @Column(name="event_name")
	  private String eventName;
		
		@NotNull(message = "Pathway id cannot be empty.")
		@NotBlank(message = "Pathway cannot be empty.")
	  @Column(name="pathway_id")
	  private long pathwayId;
	  @Column(name="event_pos_row")
	  private long eventPocRow;
	  @Column(name="event_pos_col")
	  private long eventPocCol;
	  private String status;
	  @Column(name="created_date")
	  private Date createDate;
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
	public PathwayEvents() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PathwayEvents(String eventName, long pathwayId, long eventPocRow, long eventPocCol, String status,
			Date createDate) {
		super();
		this.eventName = eventName;
		this.pathwayId = pathwayId;
		this.eventPocRow = eventPocRow;
		this.eventPocCol = eventPocCol;
		this.status = status;
		this.createDate = createDate;
	}
	   

}
