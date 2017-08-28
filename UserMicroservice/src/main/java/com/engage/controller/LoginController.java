package com.engage.controller;


import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.engage.dao.UserDao;
import com.engage.dao.UserRolesDao;
import com.engage.dao.OrganizationDao;
import com.engage.model.User;
import com.engage.model.Organization;
import com.engage.model.UserRoles;
import com.engage.util.AdvancedEncryptionStandard;
import com.engage.util.JsonMessage;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.core.JsonToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@RestController
/**
* User MicroServices for User API Operations
* User Authentication Methods
* @author  StartUP Labs
* @version 1.0
* 
*/
public class LoginController {

@Value("${microService.URL}")
public String baseURL;
@Value("${portal.URL}")
public String portalURL;
  @Autowired
  private UserDao _userDao;
  @Autowired
  private UserRolesDao _userRolesDao; 
  @Autowired 
  private OrganizationDao _organizationDao;
  
  /**
   * User Login Method
   * @Inputparam user
   * @return JsonObject
   */
 
  @RequestMapping(value="/login",method = RequestMethod.POST)

  public @ResponseBody JsonMessage login(@RequestBody final User user) {
	  JsonMessage response=new JsonMessage();
    try {
    	
     User validateUser= _userDao.getByUserName(user.getEmail(),AdvancedEncryptionStandard.encrypt(user.getPassword()));
      if(!validateUser.getEmail().equalsIgnoreCase(null))
      { 
    	if(validateUser.getStatus()=="N" )
    	{
    		response.setMessage("Account is disbled.");
    		response.setStatuscode(401);
    		 return response ;
    	}
    	 Map<String,Object> data1=new HashMap<String,Object>();
    	 data1.put("UserBacsicInfo", validateUser);
 
    	 Date today=new Date();
    	 long ltime=today.getTime()+1*24*60*60*1000;
    	 Date expDate=new Date(ltime);
    	 String token= Jwts.builder().setSubject(validateUser.getEmail()).setIssuedAt(new Date()).setExpiration(expDate)
    			 .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
    	data1.put("token",  token);
    	  response.setMessage("User login successfully.");
    	  response.setData(data1);
 
    	  response.setStatuscode(200);
    	  return response ;
      }else
      {
    	  response.setMessage("Incorrect Email/Pasword combination. Please try again.");
    	  response.setStatuscode(204);
      return response;
      }
    }
    catch(Exception ex) {
    	response.setMessage(ex.getMessage());
    	 
    	  response.setStatuscode(204);
      return response;
    }
  }
  
  /**
   * User Registration Method
   * @Inputparam user
   * @return JsonObject
   */
  @RequestMapping(value="/registration",method = RequestMethod.POST)
  public @ResponseBody JsonMessage create(@RequestBody final User user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	
    	System.out.println((_userDao.getByEmail(user.getEmail())).size());
    	if((_userDao.getByEmail(user.getEmail())).size()>0)
    	{
    		response.setMessage("Email already exists.");
       	  	response.setStatuscode(208);
       	  	return response;
    	}else
    	{
    		
    	
    		Organization org =new Organization();
    		org.setName(user.getPracticeName());
    	
    		Integer orgid=_organizationDao.save(org);
    		user.setOrgid(orgid);
		user.setPassword(AdvancedEncryptionStandard.encrypt(user.getPassword()));
		user.setUserType("A");
		user.setStatus("Y");
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		 
		  user.setCreateDate(timestamp);
		  user.setUpdateDate(timestamp);
		
		
		BigInteger id=_userDao.save(user);
		
		UserRoles userRoles=new UserRoles();
		userRoles.setUserId(id);
		userRoles.setRoleId(1);
		_userRolesDao.save(userRoles);
		RestTemplate restTemplate = new RestTemplate();
		Map<String,Object> data1=new HashMap<String,Object>();
		 data1.put("from","EngageApp<mr.anupamroy@gmail.com>");
		 data1.put("to",user.getEmail());
		 data1.put("subject","Account Confirmation");
		 data1.put("text","Hi <b>"+user.getFullName()+",</b><br><br>Congratulations! Your account has been created. Please click on the link to verify your email address and start using Engage.<br><br><a href='"+portalURL+"/userconfirmation.html?keyconfirm="+AdvancedEncryptionStandard.encrypt(user.getEmail())+"'>Verify</a><br><br>Thank You,<br>Team Engage at Quantified Care");
		 data1.put("status",true);
//		 restTemplate.postForObject("http://35.166.195.23:8080/EmailMicroservice/email/send", data1,String.class );
		 restTemplate.postForObject("http://localhost:8080/EmailMicroservice/email/send", data1,String.class );
			
		 response.setMessage("User registred successfully");
		response.setStatuscode(200);
		return response;
    	}
    }
    catch(Exception ex) 
    {
    	  response.setMessage(ex.getMessage());
    	  response.setStatuscode(203);
    	  return response;
    }
  }
  /**
   * Loading Patient Pathway Block anonymous call
   * @Inputparam user
   * @return JsonObject
   */
  
  @RequestMapping(value="/getPatientpathwayblockbyId",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getPatientpathwayblockbyId(@RequestBody Map<String, String> json) 
  {
	 
	  JsonMessage response=new JsonMessage();
	    try {
	    	Integer rid=Integer.parseInt(json.get("id").toString());
	    	
	    	
	    	List resulst=_userDao.getPatientpathwayblockById(rid);
	    		response.setMessage("Scheduled data.");
	    		response.setData(resulst);
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
   * Loading Patient by DOB anonymous call
   * @Inputparam user
   * @return JsonObject
   */
  
  @RequestMapping(value="/getPatientBydob",method = RequestMethod.POST)
  public  @ResponseBody JsonMessage  getPatientBydob(@RequestBody  Map<String, String> json ) 
  {
	  JsonMessage response=new JsonMessage();
	  try
	  {
		  String pdob=json.get("dob");
	
		 int result= _userDao.verifyPatientInfobydob(pdob);
		  response.setData(result);
			response.setStatuscode(200);
			return response; 
	  }catch(Exception ex)
	 {
		  response.setMessage(ex.getMessage());
			response.setStatuscode(200);
			return response; 
	 }
	 	
  }
  /**
   * Patient Reply Call Back for Twilio (anonymous call)
   * @Inputparam user
   * @return JsonObject
   */
  @RequestMapping(value="/userreply",method = RequestMethod.POST)
  public ResponseEntity<String> receiveBody(@RequestParam("From") String From, @RequestParam("Body") String Body)
  {
	  JsonMessage response=new JsonMessage();
	  JsonMessage response1=new JsonMessage();
	  HttpHeaders responseHeaders = new HttpHeaders();
	  String fromuser=From;
	  String userbody=Body;
	  String pexist="no";
	  String resmessage="";
	  try
	  {
		 
		  if(userbody.equals("Y"))
		  {
		
		  
		RestTemplate restTemplate = new RestTemplate();
			Map<String,Object> data1=new HashMap<String,Object>();
			 data1.put("From",fromuser);
			 data1.put("Body",userbody);
//			 String ph=fromuser.substring(3);//for India/
			 String ph=fromuser.substring(2);//for US/
			 

			 
			 ArrayList patients=_userDao.patientidbyphone(ph);
			 	 
			 Iterator itr = patients.iterator();
		      
		      while(itr.hasNext()) {
		         Object element = itr.next();
		         
		     
		         if(_userDao.verifyPatientInfo(Long.parseLong(element.toString()))>0)
		         {
		        	 pexist="yes";
		        	  
		         }
		         else
		         {
		        	 
		    		 	 
		         }
		         
		      }
		      if(pexist.equals("yes"))
		      {
		    	  resmessage="Hi there! At this time we are not monitoring our mailbox and your response will not be received. For any questions or concerns contact your Transition Specialist at 1-800-981-5904";
		      
		      }
		      if(pexist.equals("no"))
		      {

		    	  resmessage="Thank you for joining. You will start receiving messages.";
		      
		      }
		      String res=restTemplate.postForObject("http://localhost:8080/PatientMicroservice/api/v1/patientreply", data1,String.class );
//	    	String res=restTemplate.postForObject("http://localhost:8081/api/v1/patientreply", data1,String.class );
		      JsonFactory factory = new JsonFactory();
			 JsonParser parser = factory.createParser(res);
			 while (!parser.isClosed()) {
				 JsonToken token = parser.nextToken();
				 if (JsonToken.FIELD_NAME.equals(token) && "message".equals(parser.getCurrentName())) {
						token = parser.nextToken();
						resmessage=parser.getText();
						//System.out.println(resmessage);
					} 
			 }
		      return new ResponseEntity<String> (resmessage, responseHeaders,HttpStatus.OK);
		  }
		  else
		  {
			  return new ResponseEntity<String> ("Hi there! At this time we are not monitoring our mailbox and your response will not be received. For any questions or concerns contact your Transition Specialist at 1-800-981-5904", responseHeaders,HttpStatus.OK);  
		  }
	  }
	  catch(Exception ex) 
	    {
		  return new ResponseEntity<String> ("Hi there! At this time we are not monitoring our mailbox and your response will not be received. For any questions or concerns contact your Transition Specialist at 1-800-981-5904", responseHeaders,HttpStatus.OK);
	    }
	    }
  /**
   * 
   * Organization Create Call
   * @Inputparam organization JsonObject
   * @return JsonObject
   */
  
  @RequestMapping(value="/organizationcreate",method = RequestMethod.POST)
  public @ResponseBody JsonMessage organizationcreate(@RequestBody final Organization organization) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	
    	Integer orgid=_organizationDao.save(organization);
		 response.setMessage("User registred successfully");
		 response.setData(orgid);
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
   * 
   * Check for email existence
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  
  
  
  @RequestMapping(value="/IsEmailExists",method = RequestMethod.POST)
  public @ResponseBody JsonMessage IsEmailExists(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
		 if((_userDao.getByEmail(json.get("emailid")).size()>0))
	  {
		  response.setMessage("Email Already Exists");
		  response.setStatuscode(200);
		  return response;
	  }else
	  {
		  response.setMessage("No profile information found.");
		  response.setStatuscode(204);
		  return response;
	  }
	  }catch(Exception ex)
	 {
		  response.setMessage("No profile information found.");
		  response.setStatuscode(204);
		  return response;
	 }
  }
  /**
   * 
   * Verify User email existence
   * @Inputparam organization JsonObject
   * @return JsonObject
   */

  @RequestMapping(value="/verify_email",method = RequestMethod.POST)
  public @ResponseBody JsonMessage verifyEmail(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	  try 
	  {
	if(!(_userDao.getByEmail(AdvancedEncryptionStandard.decrypt(json.get("emailid"))).size()>0))
	  {
		  response.setMessage("Invalid account.");
		  response.setStatuscode(204);
		  return response;
	  }else
	  {
		  User user=_userDao.getById(AdvancedEncryptionStandard.decrypt(json.get("emailid")));
		  user.setStatus("Y");
		  _userDao.update(user);
		  response.setMessage("Account activated successfully.");
		  response.setStatuscode(200);
		  return response;
	  }
	  }catch (Exception e) {
		  response.setMessage(e.getMessage());
		  response.setStatuscode(203);
		  return response;
	}
  }
  /**
   * 
   * Verify Forgot Password Method
   * @Inputparam organization JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/verify_emailForgetpwd",method = RequestMethod.POST)
  public @ResponseBody JsonMessage resetPassword(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 if((_userDao.getByEmail(AdvancedEncryptionStandard.decrypt(json.get("emailid"))).size()>0))
		  {
			 User user=_userDao.getById(AdvancedEncryptionStandard.decrypt(json.get("emailid")));
			  user.setPassword(AdvancedEncryptionStandard.encrypt(json.get("password")));
			  _userDao.update(user);
			  response.setMessage("Password updated successfully.");
			  response.setStatuscode(200);
			  return response;
		  }else
		  {
			  response.setMessage("Email not registered.");
			  response.setStatuscode(204);
			  return response;
		  }
	 }catch (Exception e) {
		 response.setMessage(e.getMessage());
		  response.setStatuscode(203);
		  return response;
	}
	 	 
  }
  
  /**
   * 
   * Load Org names Method
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  
  @RequestMapping(value="/getAllPraticeNames",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getAllPraticeNames(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 Map<String,Object> data=new HashMap<String,Object>();
		 data.put("praticenames", _userDao.getallPraticeNames());
			  response.setMessage("List of Praticenames.");
	    	  response.setData(data);
			  response.setStatuscode(200);
			  return response;
		  
	 }catch (Exception e) {
		 response.setMessage(e.getMessage());
		  response.setStatuscode(203);
		  return response;
	}	 
  }
  /**
   * 
   * User Forgot API Call
   * @Inputparam organization JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/forget_password",method = RequestMethod.POST)
  public @ResponseBody JsonMessage forgetPassword(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try{
		 if((_userDao.getByEmail(json.get("emailid")).size()>0))
		  {
			 User user=_userDao.getById(json.get("emailid"));
			  response.setMessage("Password sent to your email.");
			  response.setStatuscode(200);
			  RestTemplate restTemplate = new RestTemplate();
				Map<String,Object> data=new HashMap<String,Object>();
				data.put("from","EngageApp<bhanu735@gmail.com>");
				data.put("to",user.getEmail());
				data.put("subject","Forgot Password");
				data.put("text","Dear <b>"+user.getFullName()+",</b><br><br>Please click on the link below to reset your password.<br><br><a href='"+portalURL+"/forgotkeyconfirm.html?keyconfirm="+AdvancedEncryptionStandard.encrypt(user.getEmail())+"'>Reset Your Password</a><br><br>Regards,<br>Team Engage.");
				data.put("status",true);
//			  restTemplate.postForObject("http://35.166.195.23:8080/EmailMicroservice/email/send", data,String.class );
			  restTemplate.postForObject("http://localhost:8080/EmailMicroservice/email/send", data,String.class );
//			  restTemplate.postForObject("http://localhost:8080/email/send", data,String.class );
			  
			  return response;
		  }else
		  {
			  response.setMessage("Email not registered.");
			  response.setStatuscode(204);
			  return response;
		  }
	 }catch (Exception e) {
		 response.setMessage(e.getMessage());
		  response.setStatuscode(204);
		  return response;
	}	  
  }

} 