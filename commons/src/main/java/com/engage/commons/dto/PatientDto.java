package com.engage.commons.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.engage.commons.validators.annotations.ValidPhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * {@link PatientDto} is created because {@link Patient} class coded in
 * Engage1.0 has setters with encryption embedded(HIPPA compliance). Hence,
 * Alphabetic and Alphanumeric validations doesn't work
 * 
 * PatientDto should be used only for Add patient and Edit patient
 * 
 * @author ai
 *
 */
public class PatientDto {
	/**
	 * No user of
	 */

	private long id;

	/**
	 * Current not present in Client side
	 */
	
	@Email
	private String email;
	@NotNull(message = "First name cannot be empty")
	@NotBlank(message = "First name cannot be empty")
	@Length(max = 60, message = "First name exceeds {max} characters.")
	@Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Only alphabet characters are allowed.")
	private String firstName;

	@NotNull(message = "Last name cannot be empty")
	@NotBlank(message = "Last name cannot be empty")
	@Length(max = 60, message = "Last name exceeds {max} characters.")
	@Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Only alphabet characters are allowed.")
	private String lastName;

	/**
	 * Find whether phone gets saved in db
	 */
	@NotNull(message = "Phone number cannot be empty")
	@NotBlank(message = "Phone number cannot be empty")
	@ValidPhoneNumber(message = "Invalid phone number format.")
	private String phone;
	private String deviceToken;
	
	
	@Pattern(regexp="^[YNyn]*$", message="Invalid Status")
	@Length(min = 1, max = 1, message = "Invalid Status")
	private String status;
	private Timestamp createDate;

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	private Timestamp updateDate;

	@NotNull(message = "Invalid Date of birth")
	@Past(message = "Invalid Date of birth")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="EST")
	private Date dob;

	/*@Min(value = 1, message = "Invalid Clinician Id")
	@Max(value = Integer.MAX_VALUE, message = "Invalid Clinician Id")*/
	private long clinicianId;

	@Min(value = 1, message = "Invalid Organization Id")
	// Since db has bigint, it is safe to user Integer.MAX_VALUE
	@Max(value = Integer.MAX_VALUE, message = "Invalid Organization Id")
	private long orgId;

	@Transient
	private int[] events;

	@Transient
	private int pathwayId;
	@Transient
	private String pathwayName;

	public String getPathwayName() {
		return pathwayName;
	}

	public void setPathwayName(String pathwayName) {
		this.pathwayName = pathwayName;
	}

	public int getPathwayId() {
		return pathwayId;
	}

	public void setPathwayId(int pathwayId) {
		this.pathwayId = pathwayId;
	}

	public int[] getEvents() {
		return events;
	}

	public void setEvents(int[] events) {
		this.events = events;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public PatientDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getClinicianId() {
		return clinicianId;
	}

	public void setClinicianId(long clinicianId) {
		this.clinicianId = clinicianId;
	}

	public long getorgId() {
		return orgId;
	}

	public void setorgId(long orgId) {
		this.orgId = orgId;
	}

	public PatientDto(String email, String firstName, String lastName, String phone, String deviceToken, String status,
			Timestamp createDate, Timestamp updateDate, Date dob, long clinicianId, long orgId) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.deviceToken = deviceToken;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.dob = dob;
		this.clinicianId = clinicianId;
		this.orgId = orgId;
	}

	public PatientDto(PatientDto patientDto) {
		this.email = patientDto.email;
		this.firstName = patientDto.firstName;
		this.lastName = patientDto.lastName;
		this.phone = patientDto.phone;
		this.deviceToken = patientDto.deviceToken;
		this.status = patientDto.status;
		this.createDate = patientDto.createDate;
		this.updateDate = patientDto.updateDate;
		this.dob = patientDto.dob;
		this.clinicianId = patientDto.clinicianId;
		this.orgId = patientDto.orgId;
	}
}
