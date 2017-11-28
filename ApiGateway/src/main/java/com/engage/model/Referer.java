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
 * <h3>Written to validate Referer header in request urls <h3>
 * <p> Soft Delete is used</p>
 * @author ai
 *
 */
@Entity
@Table(name = "qc_referer")
@SQLDelete(sql = "UPDATE qc_referer SET is_active='N' WHERE id=?")
@Where(clause = "is_active = 'Y'")
public class Referer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "Referer name cannot be blank")
	@NotBlank(message = "Referer name cannot be blank")
	@Column(name = "referer")
	private String referer;
	
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
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
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
