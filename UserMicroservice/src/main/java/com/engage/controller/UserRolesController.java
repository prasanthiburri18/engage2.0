package com.engage.controller;

import com.engage.dao.UserDao;
import com.engage.dao.UserRolesDao;
import com.engage.model.*;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/UserRoles")
public class UserRolesController {

  @Autowired
  private UserRolesDao _userRolesDao;
  
  @RequestMapping(value="/delete")
  @ResponseBody
  public String delete(Integer id) {
    try {
      UserRoles user = new UserRoles(id);
      _userRolesDao.delete(user);
    }
    catch(Exception ex) {
      return ex.getMessage();
    }
    return "User succesfully deleted!";
  }
  @RequestMapping(value="/get-by-userroles" ,method=RequestMethod.GET)
  public @ResponseBody List getAllUserRoles()
  {
	  List lst =(List)_userRolesDao.getAll();
	  return lst;
  }
  


  @RequestMapping(value="/save",method = RequestMethod.POST)

  public ResponseEntity create(@RequestBody final UserRoles user) {
    try {
    
    	user.setCreatedat(new Date());
    	_userRolesDao.save(user);
    }
    catch(Exception ex) {
      return new ResponseEntity(ex.getMessage(), HttpStatus.OK);
    }
    return new ResponseEntity("User roles saved successfully", HttpStatus.OK);
  }

}