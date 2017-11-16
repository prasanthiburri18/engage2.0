package com.engage.commons.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PathwayPatientBlockDto {
	@JsonProperty(value = "id")
	private long id;

	@JsonProperty(value = "pathway_id")
	private long pathwayId;
	@JsonProperty(value = "patient_id")
	private long patientId;

	@JsonProperty(value = "block_id")
	private long blockId;
	 @JsonFormat(pattern="yyyy-MM-dd")
	@JsonProperty(value = "block_appointment_date")
	private Timestamp blockAppointmentDate;
	@JsonProperty(value = "block_parent_id")
	private long blockPatientId;
	@JsonProperty(value = "block_name")
	private String blockName;
	@JsonProperty(value = "block_type")
	private String blockType;

	@JsonProperty(value = "block_pos_row")
	private long blockPositionRow;
	 @JsonFormat(pattern="yyyy-MM-dd")
	@JsonProperty(value = "message_send_at")
	private Timestamp messageSentAt;

	@JsonProperty(value = "message_status")
	private String messageStatus;
	 @JsonFormat(pattern="yyyy-MM-dd")
	@JsonProperty(value = "patient_accepted_date")
	private Timestamp patientAcceptedDate;
	@JsonProperty(value = "block_pos_col")
	private long blockPositionColumn;
	@JsonProperty(value = "trigger_id")
	private long triggerId;
	@JsonProperty(value = "delivery_days_after_trigger")
	private long deliveryDaysAfterTrigger;
	@JsonProperty(value = "repeat_for_number_of_days")
	private long repeatForNoOfDays;
	@JsonProperty(value = "subject_of_message")
	private String subjectOfMessage;
	@JsonProperty(value = "body_of_message")
	private String bodyOfMessage;
	@JsonProperty(value = "remainder_of_message")
	private String reminderOfMessage;

	@JsonProperty(value = "followup_of_message")
	private String followupOfMessage;

	@JsonProperty(value = "status")
	private String Status;
	@JsonProperty(value = "created_date")
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Timestamp createdDate;
	@JsonProperty(value = "event_id")
	private long eventId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(long pathwayId) {
		this.pathwayId = pathwayId;
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
	public Timestamp getBlockAppointmentDate() {
		return blockAppointmentDate;
	}
	public void setBlockAppointmentDate(Timestamp blockAppointmentDate) {
		this.blockAppointmentDate = blockAppointmentDate;
	}
	public long getBlockPatientId() {
		return blockPatientId;
	}
	public void setBlockPatientId(long blockPatientId) {
		this.blockPatientId = blockPatientId;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getBlockType() {
		return blockType;
	}
	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}
	public long getBlockPositionRow() {
		return blockPositionRow;
	}
	public void setBlockPositionRow(long blockPositionRow) {
		this.blockPositionRow = blockPositionRow;
	}
	public Timestamp getMessageSentAt() {
		return messageSentAt;
	}
	public void setMessageSentAt(Timestamp messageSentAt) {
		this.messageSentAt = messageSentAt;
	}
	public String getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	public Timestamp getPatientAcceptedDate() {
		return patientAcceptedDate;
	}
	public void setPatientAcceptedDate(Timestamp patientAcceptedDate) {
		this.patientAcceptedDate = patientAcceptedDate;
	}
	public long getBlockPositionColumn() {
		return blockPositionColumn;
	}
	public void setBlockPositionColumn(long blockPositionColumn) {
		this.blockPositionColumn = blockPositionColumn;
	}
	public long getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(long triggerId) {
		this.triggerId = triggerId;
	}
	public long getDeliveryDaysAfterTrigger() {
		return deliveryDaysAfterTrigger;
	}
	public void setDeliveryDaysAfterTrigger(long deliveryDaysAfterTrigger) {
		this.deliveryDaysAfterTrigger = deliveryDaysAfterTrigger;
	}
	public long getRepeatForNoOfDays() {
		return repeatForNoOfDays;
	}
	public void setRepeatForNoOfDays(long repeatForNoOfDays) {
		this.repeatForNoOfDays = repeatForNoOfDays;
	}
	public String getSubjectOfMessage() {
		return subjectOfMessage;
	}
	public void setSubjectOfMessage(String subjectOfMessage) {
		this.subjectOfMessage = subjectOfMessage;
	}
	public String getBodyOfMessage() {
		return bodyOfMessage;
	}
	public void setBodyOfMessage(String bodyOfMessage) {
		this.bodyOfMessage = bodyOfMessage;
	}
	public String getReminderOfMessage() {
		return reminderOfMessage;
	}
	public void setReminderOfMessage(String reminderOfMessage) {
		this.reminderOfMessage = reminderOfMessage;
	}
	public String getFollowupOfMessage() {
		return followupOfMessage;
	}
	public void setFollowupOfMessage(String followupOfMessage) {
		this.followupOfMessage = followupOfMessage;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

}
