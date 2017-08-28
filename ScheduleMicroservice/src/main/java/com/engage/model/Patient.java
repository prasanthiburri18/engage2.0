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

import com.engage.util.AdvancedEncryptionStandard;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class Patient {

	  private long id;
	  private String email;

	  private String firstName;

	  private String lastName;
	  private String phone;

	  private String deviceToken;
	  private String status;

	  private Timestamp  createDate;

	  private Timestamp  updateDate;
	  private Date dob;
	  
	  private long clinicianId;
	
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
			return lastName;
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
	public Patient(String email, String firstName, String lastName, String phone, String deviceToken, String status,
			Timestamp createDate, Timestamp updateDate, Date dob, long clinicianId) {
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
	}

	
	  

}
