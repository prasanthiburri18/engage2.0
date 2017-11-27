package com.engage.commons.dto;

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
