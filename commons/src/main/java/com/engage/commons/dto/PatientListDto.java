/**
 * 
 */
package com.engage.commons.dto;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>This class extends PatientDto.</p> 
 * It wraps events names of patient-pathway fetched from pathway microservice
 * @author ai
 *
 */
public class PatientListDto extends PatientDto{


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
public Set<String> getEventNames() {
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
