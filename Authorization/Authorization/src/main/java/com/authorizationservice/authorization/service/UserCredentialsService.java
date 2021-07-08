package com.authorizationservice.authorization.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.authorizationservice.authorization.exceptions.LoginException;
import com.authorizationservice.authorization.model.UserCredentials;
import com.authorizationservice.authorization.repository.UserCredentialsRepo;

@Service
public class UserCredentialsService {

	
	@Autowired
	UserCredentialsRepo userCredentialsRepo;
	
//	@Override
//	public UserDetails loadUserByUsername(String s) {
//
//		// fetch the details from DB and crosscheck against String s
//		// repo.findByUserName(s);
//		
//		UserCredentials auth = userCredentialsRepo.findByUsername(s);
//
//		return new User(auth.getUsername(), auth.getPassword(), new ArrayList<>());
//
//		// return null if user details don't match
//	}
	public Optional<UserCredentials> getUserByUserName(String username) {
		return userCredentialsRepo.findById(username);
	}

	public void save(UserCredentials obj) {
		userCredentialsRepo.save(obj);
	}
}
