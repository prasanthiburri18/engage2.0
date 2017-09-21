package com.engage.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.engage.commons.exception.ConstraintViolationException;
import com.engage.commons.validators.utils.ConstraintValidationUtils;
import com.engage.dao.UserRolesDao;
import com.engage.model.User;
import com.engage.model.UserRoles;

@RestController
@RequestMapping(value="/UserRoles")
public class UserRolesController {

  @Autowired
  private UserRolesDao _userRolesDao;

  @Autowired
  private Validator validator;
  
  @RequestMapping(value="/delete", method=RequestMethod.DELETE)
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
    
    //Engage2.0 start
    	
    	Set<ConstraintViolation<UserRoles>> violations =validator.validate(user);
    	if(!violations.isEmpty()){
    		//Map<String, ConstraintViolation<User>> errors = violations.stream().collect(Collectors.toMap(ConstraintViolation::getMessage, Function.identity()));
    		Set<String> errormessages = ConstraintValidationUtils.getArrayOfValidations(violations);
    		throw new ConstraintViolationException(errormessages.toString());
    	}
    //Engage2.0 end	
    	user.setCreatedat(new Date());
    	_userRolesDao.save(user);
    }
    catch(Exception ex) {
      return new ResponseEntity(ex.getMessage(), HttpStatus.OK);
    }
    return new ResponseEntity("User roles saved successfully", HttpStatus.OK);
  }

}