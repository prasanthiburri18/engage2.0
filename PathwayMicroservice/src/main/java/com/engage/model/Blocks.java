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
@Table(name = "qc_pathway_block")
public class Blocks {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull(message = "Block name cannot be empty.")
	@NotBlank(message = "Block name cannot be empty.")
	@Length(max = 30, message = "Block name exceeds {max} characters.")
	@Pattern(regexp = "^[a-zA-z0-9\\s]*$", message = "Only alphanumeric characters are allowed.")
	@Column(name = "block_name")
	private String blockName;
	@Column(name = "block_type")
	private String blockType;

	@Min(value = 1, message = "Invalid Pathway Id")
	@Max(value = Integer.MAX_VALUE, message = "Invalid Pathway Id")
	@Column(name = "pathway_id")
	private long pathwayId;

	@Min(value = 1, message = "Invalid Event Id")
	@Max(value = Integer.MAX_VALUE, message = "Invalid Event Id")
	@Column(name = "event_id")
	private long eventId;
	@Column(name = "block_pos_row")
	private long blockPocRow;
	@Column(name = "block_pos_col")
	private long blockPocCol;
	@Column(name = "trigger_id")
	private long triggerId;
	@Column(name = "delivery_days_after_trigger")
	private long DeliveryDaysAfterTigger;
	@Column(name = "repeat_for_number_of_days")
	private long repeatForNoOfDays;
	@Length(max = 500, message = "Subject of the message exceeds {max} characters.")
	@Pattern(regexp = "^[\\p{Alnum}\\p{Punct}\\s]*$", message = "Only alphanumeric characters and punctuations are allowed.")
	@Column(name = "subject_of_message")
	private String subjectOfMessage;
	private String status;

	@NotNull(message = "Block message cannot be empty.")
	@NotBlank(message = "Block message cannot be empty.")
	@Length(max = 500, message = "Block message name exceeds {max} characters.")
	//@Pattern(regexp = "^[a-zA-z0-9\\.\\?\\!,\\:\\;\"\']*$", message = "Only alphanumeric characters and punctuations are allowed.")
	@Pattern(regexp = "^[\\p{Alnum}\\p{Punct}\\s]*$", message = "Only alphanumeric characters and punctuations are allowed.")
	@Column(name = "body_of_message")
	private String bodyOfMessage;

	@Column(name = "phi_secured")
	private String phiSecured;
	@Column(name = "block_appointment_parent")
	private long blockAppointmentParent;
	
	@Length(max = 500, message = "Reminder message name exceeds {max} characters.")
	@Pattern(regexp = "^[\\p{Alnum}\\p{Punct}\\s]*$", message = "Only alphanumeric characters and punctuations are allowed.")
	//@Pattern(regexp = "^[a-zA-z0-9\\.\\?\\!,\\:\\;\"\']*$", message = "Only alphanumeric characters and punctuations are allowed.")
	@Column(name = "remainder_of_message")
	private String remainderOfMessage;
	
	@Length(max = 500, message = "Follow up message  exceeds {max} characters.")
	@Pattern(regexp = "^[\\p{Alnum}\\p{Punct}\\s]*$", message = "Only alphanumeric characters and punctuations are allowed.")
	//@Pattern(regexp = "^[a-zA-z0-9\\.\\?\\!,\\:\\;\"\']*$", message = "Only alphanumeric characters and punctuations are allowed.")
	@Column(name = "followup_of_message")
	private String followupOfMessage;

	@Column(name = "created_date")
	private Date createDate;
	@Column(name = "no_of_occurence")
	private long noofOccurence;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// phi secured
	public String getphiSecured() {
		return phiSecured;
	}

	public void setphiSecured(String phiSecured) {
		this.phiSecured = phiSecured;
	}

	public long getblockAppointmentParent() {
		return blockAppointmentParent;
	}

	public void setblockAppointmentParent(long blockAppointmentParent) {
		this.blockAppointmentParent = blockAppointmentParent;
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

	public long getnoofOccurence() {
		return noofOccurence;
	}

	public void setnoofOccurence(long noofOccurence) {
		this.noofOccurence = noofOccurence;
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

	public String getRemainderOfMessage() {
		return remainderOfMessage;
	}

	public void setRemainderOfMessage(String remainderOfMessage) {
		this.remainderOfMessage = remainderOfMessage;
	}

	public String getfollowupOfMessage() {
		return followupOfMessage;
	}

	public void setfollowupOfMessage(String followupOfMessage) {
		this.followupOfMessage = followupOfMessage;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Blocks() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Blocks(String blockName, String phiSecured, String blockType,
			long blockAppointmentParent, long pathwayId, long eventId,
			long blockPocRow, long blockPocCol, long triggerId,
			long deliveryDaysAfterTigger, long repeatForNoOfDays,
			String subjectOfMessage, String status, String bodyOfMessage,
			String remainderOfMessage, String followupOfMessage,
			Date createDate, long noofOccurence) {
		super();
		this.blockName = blockName;
		this.phiSecured = phiSecured;
		this.blockType = blockType;
		this.pathwayId = pathwayId;
		this.blockAppointmentParent = blockAppointmentParent;
		this.eventId = eventId;
		this.blockPocRow = blockPocRow;
		this.blockPocCol = blockPocCol;
		this.triggerId = triggerId;
		DeliveryDaysAfterTigger = deliveryDaysAfterTigger;
		this.repeatForNoOfDays = repeatForNoOfDays;
		this.subjectOfMessage = subjectOfMessage;
		this.status = status;
		this.bodyOfMessage = bodyOfMessage;
		this.followupOfMessage = followupOfMessage;
		this.remainderOfMessage = remainderOfMessage;
		this.createDate = createDate;
		this.noofOccurence = noofOccurence;
	}

}
