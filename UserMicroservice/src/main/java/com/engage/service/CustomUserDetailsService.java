/**
 * 
 */
package com.engage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.engage.dao.UserDao;
import com.engage.dao.UserRolesDao;
import com.engage.model.User;
import com.engage.model.UserRoles;

/**
 * Customized UserDetailsService based on project specs
 * @author mindtech-labs
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	/**
	 * Get user details
	 */
	@Autowired
	private UserDao userDao;
	
	/**
	 * Get user roles
	 */
	
	private UserRolesDao userRolesDao;
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userDao.getById(email);
		UserRoles userRoles = userRolesDao.getByuserId(user.getId());
				UserDetails userDetails = convertToUserDetails(user, userRoles);
		return userDetails;
	}
/**
 * Convert User and UserRoles combination to UserDetails
 * @param user
 * @param userRoles
 * @return
 */
	private UserDetails convertToUserDetails(final User user, final UserRoles userRoles) {
		
		
		if(user!=null && userRoles!=null){
			
		//	userRoles.getRoleId()
		}
		
	//	UserDetails userDetails = new org.springframework.security.core.userdetails.User();
		return null;
	}


}
