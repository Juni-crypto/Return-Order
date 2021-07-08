package com.authorizationservice.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authorizationservice.authorization.model.AuthenticationRequest;
import com.authorizationservice.authorization.model.UserCredentials;
import com.authorizationservice.authorization.repository.AuthRequestRepo;
import com.authorizationservice.authorization.repository.UserCredentialsRepo;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	AuthRequestRepo authRequestRepo;
	
	@Autowired
	UserCredentialsRepo userCredentialsRepo;
	
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//		log.info("BEGIN - [loadUserByUsername()]");
//		log.debug("Username : " + s);
//    	AuthenticationRequest authenticationRequest = authRequestRepo.findById(s).orElseThrow();
//    	UserDetails user = new User(authenticationRequest.getUserName(), authenticationRequest.getPassword(), new ArrayList<>());
//		log.debug("User : " + user);
//		log.info("END - [loadUserByUsername()]");
//		return user;
//    }
	
	@Override
	public UserDetails loadUserByUsername(String s) {

		// fetch the details from DB and crosscheck against String s
		// repo.findByUserName(s);
		
		UserCredentials auth = userCredentialsRepo.findByUsername(s);

		return new User(auth.getUsername(), auth.getPassword(), new ArrayList<>());

		// return null if user details don't match
	}

    public Optional<UserCredentials> getUserByUserName(String username) {
		return userCredentialsRepo.findById(username);
	}

	public void save(UserCredentials obj) {
		userCredentialsRepo.save(obj);
	}

    
} 
  