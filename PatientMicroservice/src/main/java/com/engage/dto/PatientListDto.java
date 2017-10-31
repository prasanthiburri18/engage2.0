/**
 * 
 */
package com.engage.dto;


/**
 * @author ai
 *
 */
public class PatientListDto extends PatientDto{


	private PathwayDto pathwayDto;
	
	public PatientListDto(PatientDto patientDto, PathwayDto pathwayDto2) {
		super(patientDto);
		this.pathwayDto=pathwayDto2;
		
	}
	/**
	 * @return the pathwayDto
	 */
	public PathwayDto getPathwayDto() {
		return pathwayDto;
	}
	/**
	 * @param pathwayDto the pathwayDto to set
	 */
	public void setPathwayDto(PathwayDto pathwayDto) {
		this.pathwayDto = pathwayDto;
	}
	
	
	
}
