package com.engage.commons.dto;
/**
 * <p>
 * Data Transfer Object wraps each record's patient's count of pathwaylist.
 *</p>
 *Sent from patient microservice to pathway microservice
 * @author mindtechlabs
 *
 */
public class PathwayPatientCountDto {
	
	private Long pathwayId;
	private Long pathwayCount;
	public Long getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(Long pathwayId) {
		this.pathwayId = pathwayId;
	}
	public Long getPathwayCount() {
		return pathwayCount;
	}
	public void setPathwayCount(Long pathwayCount) {
		this.pathwayCount = pathwayCount;
	}
	

}
