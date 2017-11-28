/**
 * 
 */
package com.engage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.NotBlank;


/**
 * <p>Written to validate host header in request urls </p>
 * <p> Soft Delete is used</p>
 * @author ai
 *
 */
@Entity
@Table(name = "qc_host")
@SQLDelete(sql = "UPDATE qc_host SET is_active='N' WHERE id=?")
@Where(clause = "is_active = 'Y'")
public class Host {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "Host name cannot be blank")
	@NotBlank(message = "Host name cannot be blank")
	@Column(name = "host")
	private String host;
	
	@Pattern(regexp="^[YN]*$", message="Invalid status")
	@Column(name="is_active")
	private Character isActive;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the isActive
	 */
	public Character getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Character isActive) {
		this.isActive = isActive;
	}

	

}
