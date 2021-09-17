package com.userregistration.application.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.userregistration.application.config.JwtTokenUtil;
import com.userregistration.application.constant.AuthToken;
import com.userregistration.application.dto.UserCredentialsDTO;
import com.userregistration.application.dto.UserRegistrationDTO;
import com.userregistration.application.exception.InvalidCredentialsException;
import com.userregistration.application.exception.UserNameAlreadyExistsException;
import com.userregistration.application.mapper.UserRegistrationMapper;
import com.userregistration.application.model.User;
import com.userregistration.application.model.UserCredentials;
import com.userregistration.application.repository.UserCredentialsRepository;
import com.userregistration.application.repository.UserRepository;
import com.userregistration.application.service.UserOperationsService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRegistrationServiceTest {

	@InjectMocks
	UserOperationsService userOperationsService;

	@Mock
	UserCredentialsRepository userCredRepo;

	@Mock
	UserRepository userRepo;

	@Mock
	JwtTokenUtil jwtTokenUtil;

	@Mock
	UserRegistrationMapper userMapper;
	
	
	

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
	@DisplayName("User Creation :: Postive Scenario")
	@Order(1)
	public void testUserRegistrationPostive() {
		// Event

		Mockito.when(userMapper.mapToUser(userRegistrationDTO)).thenReturn(user);

		Mockito.when(userCredRepo.save(Mockito.any(UserCredentials.class))).thenAnswer(i -> {
			UserCredentials userCredentials = i.getArgument(0);
			userCredentials.setUserCredId(1l);
			return userCredentials;
		});
		Mockito.when(userMapper.mapToCred(userRegistrationDTO, user)).thenReturn(userCredentials);
		ResponseEntity<?> result = userOperationsService.saveUserDetails(userRegistrationDTO);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("User Creation :: Negative Scenario")
	@Order(2)
	public void testUserRegistrationNegative() {
		// context

		Mockito.when(userCredRepo.findByUserName("Nagesh3000")).thenThrow(UserNameAlreadyExistsException.class);
		// Event and outcome
		assertThrows(UserNameAlreadyExistsException.class,
				() -> userOperationsService.saveUserDetails(userRegistrationDTO));
	}

	@Test
	@DisplayName("Check User Login Credentials :: Postive Scenario")
	@Order(3)
	public void testAuthenticateUserPostive() throws Exception {
		Mockito.when(userCredRepo.findByUserName("Nagesh")).thenReturn(Optional.of(userCredentials));

		String token = userOperationsService.generateToken(userCredentials);
		// Event
		ResponseEntity<AuthToken> result = userOperationsService.checkLoginCredential(userLoginDTO);
		// out come
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("Check User Login Credentials:: Negative Scenrio")
	@Order(4)
	public void testAuthenticateUserNegative() {
		// context
		Mockito.when(userCredRepo.findByUserName("Nagesh")).thenThrow(InvalidCredentialsException.class);
		// Event and outcome
		assertThrows(InvalidCredentialsException.class, () -> userOperationsService.checkLoginCredential(userLoginDTO));
	}

}
