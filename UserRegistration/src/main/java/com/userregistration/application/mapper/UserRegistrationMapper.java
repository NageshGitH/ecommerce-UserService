package com.userregistration.application.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.userregistration.application.dto.UserRegistrationDTO;
import com.userregistration.application.model.User;
import com.userregistration.application.model.UserCredentials;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper 
{
	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "mobileNo", expression = "java(Long.valueOf(request.getMobileNo()))")
	@Mapping(target = "firstName", expression = "java(request.getFirstName())")
	@Mapping(target = "lastName", expression = "java(request.getLastName())")
	@Mapping(target = "emailId", expression = "java(request.getEmailId())")
	@Mapping(target = "aadhaarNo", expression = "java(request.getAadhaarNo())")
	@Mapping(target = "panCardNo", expression = "java(request.getPanCardNo())")

	User mapToUser(UserRegistrationDTO request);
	
	@Mapping(target = "userCredId", ignore = true)
	@Mapping(target = "userName", expression = "java(request.getUserName())")
	@Mapping(target = "userPassword", expression = "java(passwordGenerator(request.getUserName()))")
	@Mapping(target = "role", expression = "java(role())")
	@Mapping(target="user",source="user")
	UserCredentials mapToCred(UserRegistrationDTO request,User user);
	
	default String passwordGenerator(String userName)
	{
		String password = userName.concat("@123");
		return new BCryptPasswordEncoder().encode(password);
	}
	default String role()
	{
		return  "ROLE_USER";
	}
	
	
}

