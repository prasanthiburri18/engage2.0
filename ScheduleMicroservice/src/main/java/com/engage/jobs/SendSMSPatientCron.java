package com.engage.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Component;


public class SendSMSPatientCron {
public static void main(String[] args) {
	@SuppressWarnings("deprecation")
Date date=new Date("2017-03-04 13:09:18");
//	
//	SimpleDateFormat sdfAmerica = new SimpleDateFormat("yyyy-M-dd hh:mm:ss a");
//	sdfAmerica.setTimeZone(TimeZone.getTimeZone("America/New_York"));
//	String sDateInAmerica = sdfAmerica.format("2017-03-04 13:09:18");
	System.out.println(date);
}
}
