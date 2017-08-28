package com.engage.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="dt_users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;
  private String email;
  private String password;
  @Column(name="full_name")
  private String fullName;
  private String phone;
  @Column(name="pratice_name")
  private String practiceName;
  @Column(name="orgid")
  private int orgid;
  @Column(name="user_type")
  private String userType;
  private String status;
  private Date createDate;
  private Date updateDate;
 
  public User() { }
public User(BigInteger id) {
	super();
	this.id = id;
}

public User(String email,String password, String fullName, String phone, String practiceName,
		int orgid,String userType,String status, Date createDate, Date updateDate) {
	super();
	this.email = email;
	
	this.password = password;
	this.fullName = fullName;
	this.phone = phone;
	this.practiceName = practiceName;
	this.orgid =orgid;
	this.userType = userType;
	this.status = status;
	this.createDate = createDate;
	this.updateDate = updateDate;
}
public Date getCreateDate() {
	return createDate;
}
public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}
public Date getUpdateDate() {
	return updateDate;
}
public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
}
public BigInteger getId() {
	return id;
}
public void setId(BigInteger id) {
	this.id = id;
}
public int getOrgid() {
	return orgid;
}
public void setOrgid(int orgid) {
	this.orgid = orgid;
}


public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}

public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getFullName() {
	return fullName;
}
public void setFullName(String fullName) {
	this.fullName = fullName;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getPracticeName() {
	return practiceName;
}
public void setPracticeName(String practiceName) {
	this.practiceName = practiceName;
}
public String getUserType() {
	return userType;
}
public void setUserType(String userType) {
	this.userType = userType;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
@Override
public String toString() {
	return "User [id=" + id + ", email=" + email + ", password=" + password + ", fullName=" + fullName + ", phone="
			+ phone + ", practiceName=" + practiceName + ", userType=" + userType + ", status=" + status
			+ ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
}




  
}