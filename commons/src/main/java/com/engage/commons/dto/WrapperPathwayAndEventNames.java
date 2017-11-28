/**
 * 
 */
package com.engage.commons.dto;

import java.util.List;

/**
 * This wraps pathway and event names list sent from pathway microservice.
 * Each element of list encloses a pathway details with event names.
 * @author mindtech-labs
 *
 */
public class WrapperPathwayAndEventNames {
	
	private List<PathwayAndEventNames> list;

	public List<PathwayAndEventNames> getList() {
		return list;
	}

	public void setList(List<PathwayAndEventNames> list) {
		this.list = list;
	}
	
	
}
