package com.engage.commons.dto;
/**
 * <p>
 * Data Transfer Object wraps each record of pathwaylist.
 * Contains both pathway details and patient count in that pathway</p>
 * @author mindtechlabs
 *
 */
public class PathwayListDto {

private PathwayDto pathwayDto;
private Long patientsCount;
public PathwayDto getPathwayDto() {
	return pathwayDto;
}
public void setPathwayDto(PathwayDto pathwDto) {
	this.pathwayDto = pathwDto;
}
public Long getPatientsCount() {
	return patientsCount;
}
public void setPatientsCount(Long patientsCount) {
	this.patientsCount = patientsCount;
}


}
