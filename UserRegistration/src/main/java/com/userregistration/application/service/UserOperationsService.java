package com.userregistration.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.userregistration.application.config.JwtTokenUtil;
import com.userregistration.application.constant.AuthToken;
import com.userregistration.application.dto.CartDTO;
import com.userregistration.application.dto.CartDetailsDTO;
import com.userregistration.application.dto.OrderHistoryDTO;
import com.userregistration.application.dto.OrderProductDTO;
import com.userregistration.application.dto.SearchDTO;
import com.userregistration.application.dto.SearchRequest;
import com.userregistration.application.dto.UserCredentialsDTO;
import com.userregistration.application.dto.UserDTO;
import com.userregistration.application.dto.UserRegistrationDTO;
import com.userregistration.application.exception.BankOperationsException;
import com.userregistration.application.exception.InvalidCredentialsException;
import com.userregistration.application.exception.ResourceNotFoundException;
import com.userregistration.application.exception.UserNameAlreadyExistsException;
import com.userregistration.application.feignclient.OrderServiceInterface;
import com.userregistration.application.mapper.UserRegistrationMapper;
import com.userregistration.application.model.User;
import com.userregistration.application.model.UserCredentials;
import com.userregistration.application.repository.UserCredentialsRepository;
import com.userregistration.application.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service(value = "userService")
@Slf4j
public class UserOperationsService implements UserDetailsService
{
	private static final Logger logger = LoggerFactory.getLogger(UserOperationsService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserCredentialsRepository userCredentialsRepo;

	@Autowired
	UserRegistrationMapper mapper;
	
	@Autowired
	OrderServiceInterface orderServiceInterface;
	
	@Autowired
	CircuitBreakerFactory circuitBreakerFactory;
	

    @Autowired
    JwtTokenUtil jwtTokenUtil;
	

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	public ResponseEntity<String> saveUserDetails(UserRegistrationDTO request) {
		logger.info("inside saveUserDetails  method");

		logger.info("Checking for if User Name exists.");
		if (userCredentialsRepo.findByUserName(request.getUserName()).isPresent())
			throw new UserNameAlreadyExistsException("User Name already exists.Please try with another user name");

		logger.info("Validation customer pancard no,aadhaar No and email id");
		Optional<String> result = userRepository.validateCustomer(request.getPanCardNo(), request.getAadhaarNo(),
				request.getEmailId());
		StringBuilder strMsg = new StringBuilder();
		if (result.isPresent()) {
			Arrays.asList(result.get().split(",")).stream().forEach(prop -> {
				if (prop.equalsIgnoreCase(request.getPanCardNo()))
					strMsg.append("Pan Card No ::  " + prop + ",");
				else if (prop.equalsIgnoreCase(request.getAadhaarNo()))
					strMsg.append("Aadhaar Card No ::  " + prop + " , ");
				else if (prop.equalsIgnoreCase(request.getEmailId()))
					strMsg.append("Email Id ::  " + prop + " ");

			});
		}
		if (strMsg.length() > 0)
			throw new BankOperationsException(
					strMsg.toString() + " registered with another customer.Please try with another!!!!");

		strMsg.delete(0, strMsg.length());
		User user = userRepository.save(mapper.mapToUser(request));

		UserCredentials credentials = userCredentialsRepo.save(mapper.mapToCred(request, user));
		
		strMsg.append("  Use credentials for Login: ").append("User Name:: " + credentials.getUserName())
				.append(" Password:: " + credentials.getUserPassword());

		return new ResponseEntity<>(strMsg.toString(), HttpStatus.OK);

	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserCredentials> userCred =  userCredentialsRepo.findByUserName(username);
		if (userCred.isEmpty()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(userCred.get().getUserName(), userCred.get().getUserPassword(), getAuthority());
		
	}
	
	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	
	public Optional<UserCredentials> findUserName(String userName) 
	{
		return userCredentialsRepo.findByUserName(userName);
	}

	public UserDTO findByUserName(String userName) 
	{
		UserDTO userDto = new UserDTO();
		Optional<UserCredentials> userCred = findUserName(userName);
		if(userCred.isPresent())
		{
			userDto.setUserId("" + userCred.get().getUser().getUserId());
			userDto.setUserName(userCred.get().getUserName());
		}else
		{
			throw new ResourceNotFoundException("User Name "+userName+" does not exits!...");
		}
		return userDto;
	}

	public ResponseEntity<List<SearchDTO>>findProductDetails(SearchRequest request)
	{
		return orderServiceInterface.getCategoryProductDetails(request);
	}
	
	public ResponseEntity<String> saveProductOrder(OrderProductDTO orderProDto) throws ResourceNotFoundException
	{
		
		UserCredentials  userCred = findUserName(orderProDto.getUserName()).orElseThrow(()->new ResourceNotFoundException("User Name "+orderProDto.getUserName()+" does not exits!..."));
		
		orderProDto.setUserId(""+userCred.getUser().getUserId());
		
		return orderServiceInterface.productOrder(orderProDto);

		 //return callProductOrder(orderProDto);
		
	}
	
	public ResponseEntity<List<OrderHistoryDTO>> orderHistory(String userName)
	{
		UserCredentials  userCred = findUserName(userName).orElseThrow(()->new ResourceNotFoundException("User Name "+userName+" does not exits!..."));
		//return callOrderHistory(userName,""+userCred.getUser().getUserId());
		return orderServiceInterface.orderHistory(userName,""+findByUserName(userName).getUserId());
	}
	 public ResponseEntity<AuthToken> checkLoginCredential(UserCredentialsDTO credentials) 
	 {
			logger.info("inside checkLoginCredential method");
			logger.info("Checking for if customer credentials");
			
			Optional<UserCredentials> userCred = userCredentialsRepo.findByUserName(credentials.getUserName());
			boolean flag = false; 			
			if(userCred.isPresent())
			{
				
				 if(new BCryptPasswordEncoder().matches(credentials.getUserPassword(), userCred.get().getUserPassword()))
					 flag=false;
				 else
					 flag=true;
			}else
			{
				flag= true;
			}
			
			if(flag)
			{
				throw new InvalidCredentialsException("Authetication Failed! Please provide valid User Name or Password ");
			}
			
			 final String token = generateToken(userCred.get());
			  
		      return new ResponseEntity<>(new AuthToken(token, userCred.get().getUserName(),"Authetication Success"),HttpStatus.OK);
		}
	 
		public ResponseEntity<String> callProductOrder(OrderProductDTO orderProDto) {
			org.springframework.cloud.client.circuitbreaker.CircuitBreaker circuitbreaker = circuitBreakerFactory
					.create("circuitbreaker");
			
			return circuitbreaker.run(() -> orderServiceInterface.productOrder(orderProDto),
					throwable -> getOrderServiceInfo());
		}
		
		public ResponseEntity<?> callOrderHistory(String userName,String userId) {
			org.springframework.cloud.client.circuitbreaker.CircuitBreaker circuitbreaker = circuitBreakerFactory
					.create("circuitbreaker");
			
			return circuitbreaker.run(() -> orderServiceInterface.orderHistory(userName,userId),
					throwable -> getOrderServiceInfo());
		}
		
		public String  generateToken(UserCredentials userCred)
		{
			return jwtTokenUtil.generateToken(userCred);
		}
		
		private ResponseEntity<String> getOrderServiceInfo() {
			return new ResponseEntity<>("Order Service is dowm.Please try after some time!....",
					HttpStatus.SERVICE_UNAVAILABLE);
		}

		public ResponseEntity<String> saveCart(String userName, List<CartDTO> cartDto)
		{
			
			UserCredentials  userCred = findUserName(userName).orElseThrow(()->new ResourceNotFoundException("User Name "+userName+" does not exits!...")); 
			
			return orderServiceInterface.saveCartDetails(""+userCred.getUser().getUserId(), cartDto);
			
		}

		public ResponseEntity<List<CartDetailsDTO>> getCartDetails(String userName, String cartId)
		{
			UserCredentials  userCred = findUserName(userName).orElseThrow(()->new ResourceNotFoundException("User Name "+userName+" does not exits!..."));
			return orderServiceInterface.getCartDetails(""+userCred.getUser().getUserId(), cartId);
		}
		
}
	
