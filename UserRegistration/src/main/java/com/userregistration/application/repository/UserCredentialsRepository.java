package com.userregistration.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userregistration.application.model.UserCredentials;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long>
{
	Optional<UserCredentials> findByUserName(String userName);
	
	UserCredentials findByUserNameAndUserPassword(String userName,String userPassword);
}
