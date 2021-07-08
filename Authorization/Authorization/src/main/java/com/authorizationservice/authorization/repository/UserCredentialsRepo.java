package com.authorizationservice.authorization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authorizationservice.authorization.model.UserCredentials;


public interface UserCredentialsRepo extends JpaRepository<UserCredentials, String>{

	UserCredentials findByUsernameAndPassword(String username, String password);
	UserCredentials findByUsername(String username);

}
