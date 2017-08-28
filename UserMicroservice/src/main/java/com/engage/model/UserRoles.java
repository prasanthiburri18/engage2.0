package com.engage.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="dt_user_roles")
public class UserRoles {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  @Column(name="role_id")
  private int roleId;
  @Column(name="user_id")
  private BigInteger userId;
  private Date createdat;
  
  public UserRoles() { }

  public UserRoles(int id) { 
    this.id = id;
  }
  

public UserRoles(int roleId, BigInteger userId, Date createdat) {
	super();
	this.roleId = roleId;
	this.userId = userId;
	this.createdat = createdat;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public int getRoleId() {
	return roleId;
}

public void setRoleId(int roleId) {
	this.roleId = roleId;
}

public BigInteger getUserId() {
	return userId;
}

public void setUserId(BigInteger userId) {
	this.userId = userId;
}

public Date getCreatedat() {
	return createdat;
}

public void setCreatedat(Date createdat) {
	this.createdat = createdat;
}



}