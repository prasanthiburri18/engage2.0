/**
 * 
 */
package com.engage.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
 * 
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
	@Autowired
	private UserRolesDao userRolesDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserDetails userDetails = null;
		User user = userDao.getById(email);
		if (user != null) {
			UserRoles userRoles = userRolesDao.getByuserId(user.getId());
			userDetails = convertToUserDetails(user, userRoles);
		}
		return userDetails;
	}

	/**
	 * Convert User and UserRoles combination to UserDetails
	 * 
	 * @param user
	 * @param userRoles
	 * @return
	 */
	private UserDetails convertToUserDetails(final User user, final UserRoles userRoles) {

		org.springframework.security.core.userdetails.User userDetails = null;

		if (user != null && userRoles != null) {

			Set<GrantedAuthority> grant = new HashSet<>();
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getUserType());
			grant.add(authority);
			userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					grant);
			// userRoles.getRoleId()
		}

		// UserDetails userDetails = new
		// org.springframework.security.core.userdetails.User();
		return userDetails;
	}

}
