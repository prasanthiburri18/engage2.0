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
@Table(name="qc_organization")
public class Organization {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  
  @Column(name="name")
  private String name;

  
  public Organization() { }


public Organization(String name) {
	super();
	this.name = name;

	
}

public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}



  
}