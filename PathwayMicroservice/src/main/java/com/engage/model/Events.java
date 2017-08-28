package com.engage.model;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.engage.util.AdvancedEncryptionStandard;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="qc_pathway_event")
public class Events {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	 @Column(name="event_name")
	  private String eventName;
	  @Column(name="pathway_id")
	  private long pathwayId;
	  @Column(name="event_pos_row")
	  private long eventPocRow;
	  @Column(name="event_pos_col")
	  private long eventPocCol;
	  private String status;
	  @Column(name="created_date")
	  private Date createDate;
	  @OneToMany(fetch = FetchType.EAGER)
	  @JoinColumn(name="event_id")
	  @Fetch(FetchMode.SUBSELECT)
	private Set<Blocks> blocks;
	public Set<Blocks> getBlocks() {
		return blocks;
	}
	public void setBlocks(Set<Blocks> blocks) {
		this.blocks = blocks;
	}
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
	public Events() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Events(String eventName, long pathwayId, long eventPocRow, long eventPocCol, String status,
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
