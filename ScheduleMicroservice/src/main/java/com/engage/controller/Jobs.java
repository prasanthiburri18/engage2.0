package com.engage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.engage.model.ScheduleJson;

@Component
public class Jobs {
	//2 Minute Cron
	@Value("${example.scheduledJob.enabled:false}")
	private boolean scheduledJobEnabled;
	@Scheduled(fixedRate = 120000)  // 2 min every 30 seconds
	public void pullRandomComment() {
		
		RestTemplate restTemplate = new RestTemplate();
		Map<String,Object> data1=new HashMap<String,Object>();
	
//		Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://35.166.195.23:8080/ScheduleMicroservice/api/v1/blockcronrun", data1,Object.class );
		Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://localhost:8080/ScheduleMicroservice/api/v1/firstdayblockcronrun", data1,Object.class );

	}
	//9 AM Cron
//	@Scheduled(cron = "0 0 7 * * ?",zone = "Asia/Kolkata")
	@Scheduled(cron = "0 0 9 * * ?",zone = "America/New_York")
	public void Daytwomessagesrun(){

		RestTemplate restTemplate = new RestTemplate();
		Map<String,Object> data1=new HashMap<String,Object>();
//		Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://35.166.195.23:8080/ScheduleMicroservice/api/v1/blockcronrun", data1,Object.class );
		Map<String,Object> pathway=(Map<String, Object>) restTemplate.postForObject("http://localhost:8080/ScheduleMicroservice/api/v1/blockcronrun", data1,Object.class );

	}

}
