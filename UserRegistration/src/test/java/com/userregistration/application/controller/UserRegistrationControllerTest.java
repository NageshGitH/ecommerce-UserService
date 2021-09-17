package com.userregistration.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.userregistration.application.constant.AuthToken;
import com.userregistration.application.dto.UserCredentialsDTO;
import com.userregistration.application.dto.UserRegistrationDTO;
import com.userregistration.application.exception.InvalidCredentialsException;
import com.userregistration.application.exception.UserNameAlreadyExistsException;
import com.userregistration.application.model.User;
import com.userregistration.application.model.UserCredentials;
import com.userregistration.application.service.UserOperationsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRegistrationControllerTest 
{
	
	@InjectMocks
	UserOperationsController userOperationsController;
	
	@Mock
	UserOperationsService userOperationsService;
	
	static UserCredentials userCredentials;
	static User user;
	static UserRegistrationDTO userRegistrationDTO;
	static UserCredentialsDTO userLoginDTO;
	
	@BeforeAll
	public static void setUp() {
		// mock Customer Credentials details as in database
		userCredentials = new UserCredentials();
		userCredentials.setUserName("Nagesh");
		userCredentials.setUserPassword(new BCryptPasswordEncoder().encode("sri"));

		// set values for userRegistration DTO
		userRegistrationDTO = new UserRegistrationDTO();
		userRegistrationDTO.setUserName("Nagesh3000");
		userRegistrationDTO.setAadhaarNo("123156789121");
		userRegistrationDTO.setFirstName("Mohan");
		userRegistrationDTO.setLastName("Rao");
		userRegistrationDTO.setEmailId("mohan1@gmail.com");
		userRegistrationDTO.setMobileNo("7680092889");
		userRegistrationDTO.setPanCardNo("AULPK1507J");

		userLoginDTO = new UserCredentialsDTO();
		userLoginDTO.setUserName("Nagesh");
		userLoginDTO.setUserPassword("sri");

	}
	
	@Test
	@DisplayName("Create User :: Postive Scenario")
	@Order(1)
	public void testCreateUserPostive() {
		// Event
		when(userOperationsService.saveUserDetails(userRegistrationDTO)).thenReturn(new ResponseEntity<>("Sucess", HttpStatus.OK));
		//Event
		 ResponseEntity<String> result = userOperationsController.saveUserDetails(userRegistrationDTO);
	     //out come
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	
	@Test
	@DisplayName("Create User :: Negative Scenario")
	@Order(2)
	public void testCreateUserNegative() 
	{
		// context
		when(userOperationsService.saveUserDetails(userRegistrationDTO)).thenThrow(UserNameAlreadyExistsException.class);
		// Event and outcome
		assertThrows(UserNameAlreadyExistsException.class,()-> userOperationsController.saveUserDetails(userRegistrationDTO));
	}

	@Test
	@DisplayName("Login Function: Positive Scenario")
	@Order(3)
	public void testAuthenticateUser()
	{
		String token = userOperationsService.generateToken(userCredentials);
		
		//context
		when(userOperationsService.checkLoginCredential(userLoginDTO)).thenReturn(new ResponseEntity<>(new AuthToken(token, "Nagesh","Authetication Success"),HttpStatus.OK));
		
		//Event
		ResponseEntity<?> result = userOperationsController.checkCredentials(userLoginDTO);
		
		//out come
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	@Test
	@DisplayName("Login Function: Negative Scenario")
	@Order(4)
	public void testAuthenticateUser1()
	{
		//context
		when(userOperationsService.checkLoginCredential(userLoginDTO)).thenThrow(InvalidCredentialsException.class);
		
		//Event and out come
		assertThrows(InvalidCredentialsException.class,()->userOperationsController.checkCredentials(userLoginDTO));
	}
	
	
}
