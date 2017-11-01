/**
 * 
 */
package com.engage.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import com.engage.model.PatientPathway;

/**
 * @author ai
 *
 */
public class PatientListDto extends PatientDto{

//private Long pathwayId;
//private String pathwayName;
/**
 * These are set from {@link PatientPathway}
 */
private Set<Long> eventIds;

/**
 * Based on eventId, these are set from {@link PathwayAndEventNames}
 */
private Set<String> eventNames;
private boolean isAccepted;
private Long daysSinceStart;

public PatientListDto(){
	
}
public PatientListDto(PatientDto patientDto){
	super(patientDto);
	this.eventNames=new LinkedHashSet<>();
	this.eventIds = new LinkedHashSet<>();
	this.isAccepted=false;
	this.daysSinceStart=(long) 0;
	//Added this to assign id for this dto. Don't change in PatientDto
	setId(patientDto.getId());
			
	
}
//pathway id is already present but as int
/*public Long getPathwayId() {
	return pathwayId;
}
public void setPathwayId(Long pathwayId) {
	this.pathwayId = pathwayId;
}
*/
//Pathway Name is already present in PatientDto
/*
public String getPathwayName() {
	return pathwayName;
}
public void setPathwayName(String pathwayName) {
	this.pathwayName = pathwayName;
}
*/public Set<String> getEventNames() {
	return eventNames;
}
public void setEventNames(Set<String> eventNames) {
	this.eventNames = eventNames;
}
public boolean isAccepted() {
	return isAccepted;
}
public void setAccepted(boolean isAccepted) {
	this.isAccepted = isAccepted;
}
public Long getDaysSinceStart() {
	return daysSinceStart;
}
public void setDaysSinceStart(Long daysSinceStart) {
	this.daysSinceStart = daysSinceStart;
}
/**
 * @return the eventIds
 */
public Set<Long> getEventIds() {
	return eventIds;
}
/**
 * @param eventIds the eventIds to set
 */
public void setEventIds(Set<Long> eventIds) {
	this.eventIds = eventIds;
}
@Override
public String toString() {
	return "PatientListDto [eventIds=" + eventIds + ", eventNames=" + eventNames + ", isAccepted=" + isAccepted
			+ ", daysSinceStart=" + daysSinceStart + ", getEmail()=" + getEmail() + ", getFirstName()=" + getFirstName()
			+ ", getLastName()=" + getLastName() + ", getPhone()=" + getPhone() + ", getPathwayName()="
			+ getPathwayName() + ", getPathwayId()=" + getPathwayId() + "]";
}

	
}
