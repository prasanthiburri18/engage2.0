package com.engage.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Sms {
	 
	  private String toNumber;
	  private String smsText;
	public String getToNumber() {
		return toNumber;
	}
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	  

}
