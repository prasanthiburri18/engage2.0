package com.engage.model;

import java.math.BigInteger;
import java.util.Date;

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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length.List;

import com.engage.commons.validators.annotations.ValidPhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "dt_users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigInteger id;
	/**
	 * @Email trims string then checks for pattern
	 */
	@Email(message = "Incorrect email format.")
	@NotNull(message = "Incorrect email format.")
	@NotBlank(message = "Incorrect email format.")
	// @NotEmpty(message = "Email cannot be empty")
	private String email;

	/**
	 * Constraints removed as password is not passed when a team member adds In
	 * case of registration, password is validated at controller layer
	 */
	/*
	 * @NotNull(message = "Password cannot be empty.")
	 * 
	 * @NotBlank(message = "Password cannot be empty.")
	 */
	@JsonIgnore
	private String password;
	@NotNull(message = "Full name cannot be empty.")
	@NotBlank(message = "Full name cannot be empty.")
	@Length(max = 60, message = "Full name exceeds {max} characters.")
	@Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Only alphabet characters are allowed.")
	@Column(name = "full_name")
	private String fullName;

	// @NotNull(message = "Phone number cannot be empty.")
	// @NotBlank(message = "Phone number cannot be empty.")
	//@ValidPhoneNumber(message = "Invalid phone number format.")
	@Pattern(regexp="^(?:[1-9][0-9]{9}|null|)$", message="Invalid phone number format")
	@Column(name = "phone")
	private String phone;
	@NotNull(message = "Practice name cannot be empty.")
	@NotBlank(message = "Practice name cannot be empty.")
	@Length(max = 60, message = "Practice exceeds {max} characters.")
	@Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Only alphabet characters are allowed.")
	@Column(name = "pratice_name")
	private String practiceName;
	/**
	 * Validator are in compliance Max integer length Database should be align
	 * accordingly Min as 0 and Max integer max value
	 */

	@Min(value = 0, message = "Invalid organization id.")
	@Max(value = Integer.MAX_VALUE, message = "Invalid organization id.")
	@Column(name = "orgid")
	private int orgid;
	@NotNull(message = "User type cannot be empty")
	@Pattern(regexp = "^[auAU]*$", message = "Invalid user type.")
	@Length(min = 1, max = 1, message = "Invalid user type.")
	@Column(name = "user_type")
	private String userType;
	private String status;
	private Date createDate;
	private Date updateDate;

	public User() {
	}

	public User(BigInteger id) {
		super();
		this.id = id;
	}

	public User(String email, String password, String fullName, String phone, String practiceName, int orgid,
			String userType, String status, Date createDate, Date updateDate) {
		super();
		this.email = email;

		this.password = password;
		this.fullName = fullName;
		this.phone = phone;
		this.practiceName = practiceName;
		this.orgid = orgid;
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