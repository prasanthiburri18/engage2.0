package com.engage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Length.List;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="qc_organization")
public class Organization {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  
  /**
   * Validations added as part of Engage 2.0
   * Name cannot be more than 60 characters
   */
  @NotEmpty(message="Organization name cannot be empty.")
  @NotBlank(message="Organization name cannot be blank.")
	  @Length(max=60, message="Organization name exceeds {max} characters.")
  @Pattern(regexp="^[a-zA-z\\s]*$", message="Only alphabetic characters are allowed.")
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