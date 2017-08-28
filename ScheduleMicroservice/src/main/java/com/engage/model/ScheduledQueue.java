package com.engage.model;


import java.sql.Date;
import java.sql.Timestamp;
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
@Table(name="qc_scheduled_queue")
public class ScheduledQueue {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	 @Column(name="scheduled_message_id")
	  private String scheduledMessageId;
	  @Column(name="app_id")
	  private long appId;
	  @Column(name="to_be_sent_at")
	  private Timestamp toBeSentAt;
	  public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getScheduledMessageId() {
		return scheduledMessageId;
	}
	public void setScheduledMessageId(String scheduledMessageId) {
		this.scheduledMessageId = scheduledMessageId;
	}
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
	}
	public Timestamp getToBeSentAt() {
		return toBeSentAt;
	}
	public void setToBeSentAt(Timestamp toBeSentAt) {
		this.toBeSentAt = toBeSentAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String status;
	public ScheduledQueue(String scheduledMessageId, long appId, Timestamp toBeSentAt, String status) {
		super();
		this.scheduledMessageId = scheduledMessageId;
		this.appId = appId;
		this.toBeSentAt = toBeSentAt;
		this.status = status;
	}
	 

}
