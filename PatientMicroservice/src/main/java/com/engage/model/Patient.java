package com.engage.model;


import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length.List;

import com.engage.util.AdvancedEncryptionStandard;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="qc_patient")
public class Patient {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	 
	 /**
	  * Current not present in Client side
	  */
	  private String email;
	  @NotNull(message = "First name name cannot be null")
		@NotBlank(message = "First name cannot be blank")
		@List({ @Length(min = 2, message = "First name should be a minimum of {min} characters."),
				@Length(max = 60, message = "First name exceeds {max} characters."), })
	  @Pattern(regexp = "^[a-zA-z\\s]*$", message = "Only alphabetic characters are allowed.")
	  @Column(name="first_name")
	  private String firstName;
	  
	  @NotNull(message = "Last name name cannot be null")
		@NotBlank(message = "Last name cannot be blank")
		@List({ @Length(min = 2, message = "Last name should be a minimum of {min} characters."),
				@Length(max = 60, message = "Last name exceeds {max} characters."), })
	  @Pattern(regexp = "^[a-zA-z\\s]*$", message = "Only Alphabet characters are allowed.")
	  @Column(name="last_name")
	  private String lastName;
	  
	  /**
	   * Find whether phone gets saved in db
	   */
	  private String phone;
	  @Column(name="device_token")
	  private String deviceToken;
	  private String status;
	  @Column(name="created_date")
	  private Timestamp  createDate;
	  @Column(name="updated_date")
	  private Timestamp  updateDate;
	  private Date dob;
	  
	  @Column(name="created_by")
	  private long clinicianId;
	  
	  @Column(name="org_id")
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
	public String getEmail() {
		try {
			return AdvancedEncryptionStandard.decrypt(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return email;
		}
	}
	public void setEmail(String email) {
		try {
			this.email = AdvancedEncryptionStandard.encrypt(email);
		} catch (Exception e) {
			this.email=null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getFirstName() {
		
		try {
			return AdvancedEncryptionStandard.decrypt(firstName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return firstName;
		}
	}
	
	public void setFirstName(String firstName) {
		try {
			this.firstName = AdvancedEncryptionStandard.encrypt(firstName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			this.firstName=null;
			e.printStackTrace();
		}
	}
	public String getLastName() {
		try {
			return AdvancedEncryptionStandard.decrypt(lastName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return lastName;
		}
	}
	public void setLastName(String lastName) {
		try {
			this.lastName = AdvancedEncryptionStandard.encrypt(lastName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.lastName=null;
		}
	}
	public String getPhone() {
		try {
			return AdvancedEncryptionStandard.decrypt(phone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return this.phone=null;
		}
	}
	public void setPhone(String phone) {
	
		try {
			this.phone  = AdvancedEncryptionStandard.encrypt(phone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.phone =null;
		}
	}
	public String getDeviceToken() {
		
		try {
			return AdvancedEncryptionStandard.decrypt(deviceToken);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return deviceToken;
		}
	}
	public void setDeviceToken(String deviceToken) {
		try {
			this.deviceToken = AdvancedEncryptionStandard.encrypt(deviceToken);
		} catch (Exception e) {
			this.deviceToken =null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	

	public Patient() {
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
	public Patient(String email, String firstName, String lastName, String phone, String deviceToken, String status,
			Timestamp createDate, Timestamp updateDate, Date dob, long clinicianId,long orgId) {
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

	
	  

}
