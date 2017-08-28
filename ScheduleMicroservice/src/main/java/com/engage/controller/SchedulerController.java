package com.engage.controller;

import com.engage.dao.ScheduledQueueDao;
import com.engage.model.Patient;
import com.engage.model.ScheduleJson;
import com.engage.model.ScheduledQueue;
import com.engage.model.Sms;
import com.engage.util.AdvancedEncryptionStandard;
import com.engage.util.JsonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.sdk.TwilioClient;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
@RestController
/**
* Scheduled MicroServices for Message Scheduled API Operations
*
* @author  StartUP Labs
* @version 1.0
* 
*/
@RequestMapping(value="/api/v1")
public class SchedulerController {
@Value("${twlilio.ACCOUNT_SID}")
private String ACCOUNT_SID;
@Value("${twlilio.AUTH_TOKEN}")
private String AUTH_TOKEN;
@Value("${twlilio.phone}")
private String fromNumber;
	private static Logger log = LoggerFactory.getLogger(SchedulerController.class);
  @Autowired
  private ScheduledQueueDao _scheduledQueueDao;
  @RequestMapping(value="/checksendSms",method = RequestMethod.POST)
  public @ResponseBody JsonMessage checksendSms(@RequestBody final Sms sms) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
		 final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		 final Account mainAccount = client.getAccount();
		 final SmsFactory messageFactory = mainAccount.getSmsFactory();
		 final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
		 messageParams.add(new BasicNameValuePair("To", "+919676719422")); 
		 messageParams.add(new BasicNameValuePair("From", fromNumber));
		 messageParams.add(new BasicNameValuePair("Body", "Please Ignore"));
		
		 Object re=messageFactory.create(messageParams);
		 

		 response.setMessage("Sms sent successfully");
		 
		 response.setData(re);
		  response.setStatuscode(200);
		 return response;
	  
	  }catch(Exception ex)
	 {
		  System.out.println(ex.getMessage());
		  response.setMessage(ex.getMessage());
		  response.setStatuscode(204);
		  return response;
	 }
  }
  @RequestMapping(value="/sendSms",method = RequestMethod.POST)
  public @ResponseBody JsonMessage addPathway(@RequestBody final Sms sms) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
		 final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		 final Account mainAccount = client.getAccount();
		 final SmsFactory smessageFactory = mainAccount.getSmsFactory();
		 final MessageFactory messageFactory = mainAccount.getMessageFactory();
		 final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
		 messageParams.add(new BasicNameValuePair("To", sms.getToNumber()));
		 messageParams.add(new BasicNameValuePair("From", fromNumber));
		 messageParams.add(new BasicNameValuePair("Body", sms.getSmsText()));
		

		 response.setMessage("Sms sent successfully");
		 messageFactory.create(messageParams);
		 response.setData("SMS Data");
		  response.setStatuscode(200);
		 return response;
	  
	  }catch(Exception ex)
	 {
		  System.out.println(ex.getMessage());
		  response.setMessage(ex.getMessage());
		  response.setStatuscode(204);
		  return response;
	 }
  }

	@SuppressWarnings("unchecked")
  @RequestMapping(value="/sendSmsScheduledDataorg",method = RequestMethod.POST)  // every 30 seconds
	public @ResponseBody JsonMessage pullRandomComment() {
	  JsonMessage response=new JsonMessage();
		List<ScheduleJson> scheduledData=new ArrayList<ScheduleJson>();
		try{
		RestTemplate restTemplate = new RestTemplate();
		Map<String,Object> data1=new HashMap<String,Object>();
//		List<Object> pathway=(List<Object>) restTemplate.postForObject("http://35.166.195.23:8080/PathwayMicroservice/api/v1/getScheduledData", data1,Object.class );
		List<Object> pathway=(List<Object>) restTemplate.postForObject("http://localhost:8080/PathwayMicroservice/api/v1/getScheduledData", data1,Object.class );

		//		List<Object> pathway=(List<Object>) restTemplate.postForObject("http://localhost:8081/api/v1/getScheduledData", data1,Object.class );
		
		int i=0,j=0;
		for(Object obj:pathway)
		{
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(pathway.get(i));
			ScheduleJson user = mapper.readValue(jsonInString, ScheduleJson.class);
			scheduledData.add(user);
		Map<String,Object> data2=new HashMap<String,Object>();
			data2.put("pathwayId", user.getPathwayId());
			data2.put("eventId", user.getEventId());
			RestTemplate restTemplate1= new RestTemplate();
//		Map<String,Object> patientdata=(Map<String,Object>) restTemplate1.postForObject("http://35.166.195.23:8080/PatientMicroservice/api/v1/getPatientsPathwayIdAndEventId", data2,Object.class );
		Map<String,Object> patientdata=(Map<String,Object>) restTemplate1.postForObject("http://localhost:8080/PatientMicroservice/api/v1/getPatientsPathwayIdAndEventId", data2,Object.class );

		//		Map<String,Object> patientdata=(Map<String,Object>) restTemplate1.postForObject("http://localhost:8082/api/v1/getPatientsPathwayIdAndEventId", data2,Object.class );


				 final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
				 final Account mainAccount = client.getAccount();
				 final SmsFactory messageFactory = mainAccount.getSmsFactory();
				 final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
				 messageParams.add(new BasicNameValuePair("To", "+91"+patientdata.get("phone").toString())); // Replace with a valid phone number
				 messageParams.add(new BasicNameValuePair("From", fromNumber));
				 messageParams.add(new BasicNameValuePair("Body", user.getMessage()));
				 try{
					 response.setData(messageFactory.create(messageParams));
				 }catch (Exception e) {
					// TODO: handle exception
				}
				 
			i++;
		}
		
		response.setMessage("Sms sent successfully");
		response.setStatuscode(200);
		 return response;
		
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			 response.setMessage("Sms sent successfully");
			
			  response.setStatuscode(200);
			 return response;
		}
	
	}
	
	
	/**
	   * Loading Patient Message blocks
	   * for schedule run
	   * 
	   * @Inputparam Patient JsonObject
	   * @return JsonObject
	   */
	@RequestMapping(value="/blockcronrun",method = RequestMethod.POST)
	  public @ResponseBody JsonMessage blockcronrun() 
	  {
		  JsonMessage response=new JsonMessage();
		  List<Object> Res=null;
		 
		    try {
		    	
		    	 Res=_scheduledQueueDao.getBlockcrondataexecute();
		    	 for (int i = 0; i < Res.size(); i++) {
		 			ArrayList records = (ArrayList) Res.get(i);
		 			Integer recordid=Integer.parseInt(records.get(0).toString());
		 			Integer pathwayid=Integer.parseInt(records.get(1).toString());
		 			Integer patientid=Integer.parseInt(records.get(2).toString());
		 			String pph=_scheduledQueueDao.patientinfobyPhone(patientid);
		 			Integer blockparentid=Integer.parseInt(records.get(5).toString());
		 			String blocktype=records.get(7).toString();
		 			String message="";
		 			if(blocktype.equals("M"))
		 			{
 				String bmessage=(records.get(17).toString());
		 			message=bmessage;
		 			}
		 			
		 				if (blocktype.equals("A"))
		 			{

		 					if(blocktype.equals("A") && blockparentid!=0)
			 			{
		 				
		 					String rmessage=(records.get(18).toString());
				 			String fmessage=(records.get(19).toString());

		if(!rmessage.isEmpty()  && !rmessage.equals("no message"))
		 					{
		 						
		 						message=rmessage;
		 					}
				 			if(!fmessage.isEmpty()  && !fmessage.equals("no message"))
		 					{
		 					
		 						message=fmessage;
		 					}
			 			}
		 			}
		 			String bname=records.get(6).toString();
		 			String messagestatus=records.get(10).toString();
		
		 			if(!message.equals("Signup") && !message.equals("") && messagestatus.equals("scheduled"))
		 			{
		 			
		 			String phistatus=records.get(23).toString();
		
		 				if(phistatus.equals("yes"))
		 				{
//		 					message="http://35.166.195.23:8080/users/phi.html?bid="+recordid+"&pathwayid="+pathwayid;
		 					message="https://engage-staging.quantifiedcare.com/users/phi.html?bid="+recordid+"&pathwayid="+pathwayid;
		 				
		 				}
		 			
		 			
		 			 final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
					 final Account mainAccount = client.getAccount();

					 final SmsFactory smessageFactory = mainAccount.getSmsFactory();
					 final MessageFactory messageFactory = mainAccount.getMessageFactory();
					 final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
//					 messageParams.add(new BasicNameValuePair("To", "+91"+pph)); // Replace with a valid phone number
					 messageParams.add(new BasicNameValuePair("To", "+1"+pph)); // Replace with a valid phone number
					 messageParams.add(new BasicNameValuePair("From", fromNumber)); // Replace with a valid phone number in your account
					 messageParams.add(new BasicNameValuePair("Body", message));
					 try{
						 messageFactory.create(messageParams);
						 response.setData("Message Sent");
						 _scheduledQueueDao.updatePatientmessagestatus(recordid);
				 }catch (Exception e) {
						// TODO: handle exception
					}
		 			}

		 		}
		    	 
		    		response.setData(Res);
		    		response.setStatuscode(200);
		    		return response;
		    }
		    catch(Exception ex) 
		    {
		    	  response.setMessage(ex.getMessage());
		    	  response.setStatuscode(203);
		    	  return response;
		    }
		 	 
	  }
	/**
	   * Loading Patient Message blocks
	   * for schedule run for same day message deliver
	   * 
	   * @Inputparam Patient JsonObject
	   * @return JsonObject
	   */
	@RequestMapping(value="/firstdayblockcronrun",method = RequestMethod.POST)
	  public @ResponseBody JsonMessage firstdayblockcronrun() 
	  {
		  JsonMessage response=new JsonMessage();
		  List<Object> Res=null;
		 
		    try {
		    	
		    	 Res=_scheduledQueueDao.getFirstdayBlockcrondataexecute();

		    	 for (int i = 0; i < Res.size(); i++) {
		 			ArrayList records = (ArrayList) Res.get(i);
		 			Integer recordid=Integer.parseInt(records.get(0).toString());
		 			Integer pathwayid=Integer.parseInt(records.get(1).toString());
		 			Integer patientid=Integer.parseInt(records.get(2).toString());
		 			String pph=_scheduledQueueDao.patientinfobyPhone(patientid);
		 			Integer blockparentid=Integer.parseInt(records.get(5).toString());
		 			String blocktype=records.get(7).toString();
		 			String message="";
		 			if(blocktype.equals("M"))
		 			{
		 			String bmessage=(records.get(17).toString());
		 			message=bmessage;
	 			}
				if (blocktype.equals("A"))
		 			{
					if(blocktype.equals("A") && blockparentid!=0)
			 			{
		 					
		 					String rmessage=(records.get(18).toString());
				 			String fmessage=(records.get(19).toString());
				 if(!rmessage.isEmpty()  && !rmessage.equals("no message"))
		 					{
		 				message=rmessage;
		 					}
				 			if(!fmessage.isEmpty()  && !fmessage.equals("no message"))
		 					{
		 				message=fmessage;
		 					}
			 			}
		 			}
		 			String bname=records.get(6).toString();
		 			String messagestatus=records.get(10).toString();
		 		if(!message.equals("Signup") && !message.equals("") && messagestatus.equals("scheduled"))
		 			{
		 		
		 				//checking here for phi secured
		 				String phistatus=records.get(23).toString();
	
		 				if(phistatus.equals("yes"))
		 				{
		 				
//		 					message="http://35.166.195.23:8080/users/phi.html?bid="+recordid+"&pathwayid="+pathwayid;
		 					message="https://engage-staging.quantifiedcare.com/users/phi.html?bid="+recordid+"&pathwayid="+pathwayid;
		 				
		 				}
		 		
		 			 final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
					 final Account mainAccount = client.getAccount();
					 final SmsFactory smessageFactory = mainAccount.getSmsFactory();
					 final MessageFactory messageFactory = mainAccount.getMessageFactory();
					 final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
//					 messageParams.add(new BasicNameValuePair("To", "+91"+pph));
					 messageParams.add(new BasicNameValuePair("To", "+1"+pph));
					 messageParams.add(new BasicNameValuePair("From", fromNumber));		
					 messageParams.add(new BasicNameValuePair("Body", message));
					 try{
						 messageFactory.create(messageParams);
						 response.setData("Message Sent");

				 _scheduledQueueDao.updatePatientmessagestatus(recordid);

				 }catch (Exception e) {
						// TODO: handle exception
					}
		 			}

		 			
		 
		 		}
		    	 
		    		response.setData(Res);
		    		response.setStatuscode(200);
		    		return response;
		    }
		    catch(Exception ex) 
		    {
		    	  response.setMessage(ex.getMessage());
		    	  response.setStatuscode(203);
		    	  return response;
		    }
		 	 
	  }


	
	/**
	   * Schedule Data Method
	   * for Inner rest template usage
	   * 
	   * @Inputparam Patient JsonObject
	   * @return JsonObject
	   */
	  @RequestMapping(value="/sendSmsScheduledData",method = RequestMethod.POST)  // every 30 seconds
		public @ResponseBody JsonMessage sendsmsforschduleddata() {
		  JsonMessage response=new JsonMessage();
			List<ScheduleJson> scheduledData=new ArrayList<ScheduleJson>();
			try{
			
			
			response.setMessage("Sms sent successfully");
			response.setStatuscode(200);
			 return response;
			
			}catch(Exception ex)
			{
				System.out.println(ex.getMessage());
				 response.setMessage("Sms sent successfully");
				
				  response.setStatuscode(200);
				 return response;
			}
		
		}

}