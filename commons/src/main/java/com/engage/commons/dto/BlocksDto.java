package com.engage.commons.dto;

import java.sql.Date;

public class BlocksDto {
	private long id;

	private String blockName;
	private String blockType;

	private long pathwayId;

	private long eventId;
	private long blockPocRow;
	private long blockPocCol;
	private long triggerId;
	private long DeliveryDaysAfterTigger;
	private long repeatForNoOfDays;
	private String subjectOfMessage;
	private String status;

	private String bodyOfMessage;

	private String phiSecured;
	private long blockAppointmentParent;
	
	private String remainderOfMessage;
	
	private String followupOfMessage;

	private Date createDate;
	private long noofOccurence;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public long getBlockPocRow() {
		return blockPocRow;
	}
	public void setBlockPocRow(long blockPocRow) {
		this.blockPocRow = blockPocRow;
	}
	public long getBlockPocCol() {
		return blockPocCol;
	}
	public void setBlockPocCol(long blockPocCol) {
		this.blockPocCol = blockPocCol;
	}
	public long getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(long triggerId) {
		this.triggerId = triggerId;
	}
	public long getDeliveryDaysAfterTigger() {
		return DeliveryDaysAfterTigger;
	}
	public void setDeliveryDaysAfterTigger(long deliveryDaysAfterTigger) {
		DeliveryDaysAfterTigger = deliveryDaysAfterTigger;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBodyOfMessage() {
		return bodyOfMessage;
	}
	public void setBodyOfMessage(String bodyOfMessage) {
		this.bodyOfMessage = bodyOfMessage;
	}
	public String getPhiSecured() {
		return phiSecured;
	}
	public void setPhiSecured(String phiSecured) {
		this.phiSecured = phiSecured;
	}
	public long getBlockAppointmentParent() {
		return blockAppointmentParent;
	}
	public void setBlockAppointmentParent(long blockAppointmentParent) {
		this.blockAppointmentParent = blockAppointmentParent;
	}
	public String getRemainderOfMessage() {
		return remainderOfMessage;
	}
	public void setRemainderOfMessage(String remainderOfMessage) {
		this.remainderOfMessage = remainderOfMessage;
	}
	public String getFollowupOfMessage() {
		return followupOfMessage;
	}
	public void setFollowupOfMessage(String followupOfMessage) {
		this.followupOfMessage = followupOfMessage;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getNoofOccurence() {
		return noofOccurence;
	}
	public void setNoofOccurence(long noofOccurence) {
		this.noofOccurence = noofOccurence;
	}
	

}
