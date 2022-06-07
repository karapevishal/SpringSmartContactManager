package com.smartContectManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartContectManager.Entity.User;
import com.smartContectManager.dao.UserRepository;

public class UserDetailsServiceimpl implements UserDetailsService {

	//fetching user from dao
	@Autowired
	private UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = this.repository.getUserByUserName(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Could not found user !!");
		}
		
		CustomUserDetails details=new CustomUserDetails(user);
	
		return details;
		
	}

}
