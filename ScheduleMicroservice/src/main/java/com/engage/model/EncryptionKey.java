package com.engage.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Soft Delete is implemented, although we cannot update key from front end
 * 
 * @author mindtech-labs
 *
 */
@SQLDelete(sql = "UPDATE qc_encrypt_key SET is_active='N' WHERE id=?")
@Where(clause = "IS_ACTIVE = 'Y'")
@Entity
@Table(name="qc_encrypt_key")
public class EncryptionKey {
	
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;
	
	@NotNull
	@NotEmpty
	@Column(name="key")
	private String key;
	
	/**
	 * Set as Y in constructor by default
	 */
	@Length
	@Pattern(regexp="^[YN]*$", message="Invalid status")
	@Column(name="is_active")
	private char isActive;
	/**
	 * No args constructor
	 */
	public EncryptionKey() {
		this.isActive='Y';
	}
	
	/**
	 * Initializing Entity with Constructor
	 */
	public EncryptionKey(String key) {
		this();
		this.key=key;
	}
	
	public char getIsActive() {
		return isActive;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public String getKey() {
		return key;
	}
	
	
	public void setKey(String key) {
		this.key = key;
	}
}
