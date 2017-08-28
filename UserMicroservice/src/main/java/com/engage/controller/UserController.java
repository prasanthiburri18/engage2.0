package com.engage.controller;


import com.engage.model.User;
import com.engage.model.Organization;
import com.engage.model.UserRoles;
import com.engage.util.AdvancedEncryptionStandard;
import com.engage.util.JsonMessage;
import com.engage.dao.UserDao;
import com.engage.dao.OrganizationDao;
import com.engage.dao.UserRolesDao;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
* User MicroServices for User API Operations
*
* @author  StartUP Labs
* @version 1.0
* 
*/
@RestController
@RequestMapping(value="/api/v1")
public class UserController {
	  private static Logger log = LoggerFactory.getLogger(UserController.class);
	  static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	 
  @Autowired
  private UserDao _userDao;
  @Autowired
  private UserRolesDao _userRolesDao;
  @Autowired
  private OrganizationDao _organizationDao;
  @Value("${portal.URL}")
  public String portalURL;
  
  /**
   * Load user Profile Method
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/profile",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getProfile(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
	  Map<String,Object> data=new HashMap<String,Object>();
	  
	  if(!(_userDao.getByEmail(json.get("emailid"))).isEmpty())
	  {
		  List<User> user =_userDao.getByEmail(json.get("emailid"));
		  data.put("userData", user);
		  response.setMessage("User profile");
		  response.setStatuscode(200);
		  response.setData(data);
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
   * Change Password Method
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/change_password",method = RequestMethod.POST)
  public @ResponseBody JsonMessage changePassword(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 if(!(json.get("userid")).isEmpty())
		  {
//			  User user=_userDao.getByUserName(json.get("emailid"),AdvancedEncryptionStandard.decrypt(json.get("oldpassword")));
			 BigInteger uid = new BigInteger(json.get("userid"));
			 User user=_userDao.getByUserByUid(uid);
			 user.setPassword(AdvancedEncryptionStandard.encrypt(json.get("password")));
			  _userDao.update(user);
			  response.setMessage("Password updated successfully.");
			  response.setStatuscode(200);
			  return response;
		  }else
		  {
			  response.setMessage("No User Found.");
			  response.setStatuscode(204);
			  return response;
		  }
	 }catch (Exception ex) {
		 response.setMessage(ex.getMessage());
		  response.setStatuscode(204);
		  return response;
	}
	 	 
  }
  
  /**
   * Add Team member Method
   * @Inputparam user JsonObject
   * @return JsonObject
   */

  @RequestMapping(value="/addteammember",method = RequestMethod.POST)
  public @ResponseBody JsonMessage addteammember(@RequestBody final User user) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	
    	if((_userDao.getByEmail(user.getEmail())).size()>0)
    	{
    		response.setMessage("Email already exists.");
       	  	response.setStatuscode(208);
       	  	return response;
    	}else
    	{
    		
    	
    		String pp=Long.toHexString(Double.doubleToLongBits(Math.random()));
    
		user.setPassword(AdvancedEncryptionStandard.encrypt(pp));
	
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
//		 data1.put("from","EngageApp<support@quantifiedcare.com>");
//		 data1.put("to",user.getEmail());
//		 data1.put("subject","Added as team member");
//		 data1.put("text","Hi <b>"+user.getFullName()+",</b><br><br>Congratulations! Your have been added as team member. Please login with your email and password is  "+pp+" <br> For Login <a href='"+portalURL+"'>Click Here</a><br>Thank You,<br>Team Engage at Quantified Care");
//		 data1.put("status",true);
//		 restTemplate.postForObject("http://35.166.195.23:8080/EmailMicroservice/email/send", data1,String.class );
		 //restTemplate.postForObject("http://localhost:8080/EmailMicroservice/email/send", data1,String.class );
			
		 response.setMessage("Team Member added successfully");
		response.setStatuscode(200);
		response.setData(pp);
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
   * Sending an email for created team member
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/sendemailforteammember",method = RequestMethod.POST)
  public @ResponseBody JsonMessage sendemailforteammember(@RequestBody Map<String, String> json) 
  {
	JsonMessage response=new JsonMessage();
    try 
    {
    	
    	String mailcontent=json.get("mailcontent");
    	String useremail=json.get("useremail");
    	String userpp=json.get("userpp");
    	String username=json.get("username");
  	
    		
		RestTemplate restTemplate = new RestTemplate();
		Map<String,Object> data1=new HashMap<String,Object>();
		 data1.put("from","EngageApp<support@quantifiedcare.com>");
		 data1.put("to",useremail);
		 data1.put("subject","Welcome Email");
		 data1.put("text","Hi <b>"+username+",</b><br><br>Congratulations! Your have been added as team member. Please login with your email: "+useremail+" and password is  "+userpp+" <br> For Login <a href='"+portalURL+"'>Click Here</a><br>Thank You,<br>Team Engage at Quantified Care");
		 data1.put("status",true);
//		 restTemplate.postForObject("http://35.166.195.23:8080/EmailMicroservice/email/send", data1,String.class );
		 restTemplate.postForObject("http://localhost:8080/EmailMicroservice/email/send", data1,String.class );
			
		 response.setMessage("Email sent successfully");
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
   * Load Team members  Method
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/teammemberslist",method = RequestMethod.POST)
  public @ResponseBody JsonMessage teammemberslist(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		Long orid=Long.parseLong(json.get("orgid"));
	
		List<User> users= _userDao.getByOrgids(orid);
		response.setMessage("Team Memebers List.");
		response.setData(users);
		response.setStatuscode(200);
		return response;
		  
	 }catch (Exception e) {
		 response.setMessage("No Data available.");
		  response.setStatuscode(204);
		  return response;
	}
	 	 
  }
  /**
   * Update user Profile Method
   * @Inputparam  user JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/updateprofile",method = RequestMethod.POST)
  public @ResponseBody JsonMessage updateprofile(@RequestBody final User user) 
  {
	  JsonMessage response=new JsonMessage();
	 try
	 {
		 Organization org=new Organization();
		 org.setId(user.getOrgid());
		 org.setName(user.getPracticeName());
		 
		 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		 
//		  user.setCreateDate(timestamp);
		  user.setUpdateDate(timestamp);
		  
		 _userDao.update(user);
		 _organizationDao.update(org);
		 
	response.setMessage("Profile Udated.");
	response.setStatuscode(200);
			  return response;
		  
	 }catch (Exception e) {
		 response.setMessage("Email not registered.");
		  response.setStatuscode(204);
		  return response;
	}
	 	 
  }
  /**
   * Load Organization Information Method
   * @Inputparam  JsonObject
   * @return JsonObject
   */
  @RequestMapping(value="/getorginfo",method = RequestMethod.POST)
  public @ResponseBody JsonMessage getorginfo(@RequestBody Map<String, String> json) 
  {
	  JsonMessage response=new JsonMessage();
	 try 
	  {
	  Map<String,Object> data=new HashMap<String,Object>();
	  Integer pid=Integer.parseInt(json.get("orgid").toString());
	 
		  Organization org =_organizationDao.getById(pid);
		 
		  response.setMessage("Organization Informaiton");
		  response.setStatuscode(200);
		  response.setData(org);
		  return response;
	 
		 
	  }catch(Exception ex)
	 {
		  response.setMessage("No profile information found.");
		  response.setStatuscode(204);
		  return response;
	 }
  }


}