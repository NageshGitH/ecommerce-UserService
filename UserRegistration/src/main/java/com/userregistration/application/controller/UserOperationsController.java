package com.userregistration.application.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.userregistration.application.exception.CartDetailsNotFoundException;
import com.userregistration.application.exception.ResourceNotFoundException;
import com.userregistration.application.service.UserOperationsService;
import com.userregistration.application.util.ErrorUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/user")
@EnableTransactionManagement
@Slf4j
public class UserOperationsController {

	private static final Logger logger = LoggerFactory.getLogger(UserOperationsController.class);

	@Autowired
	UserOperationsService userOperationsService;
	
	 @Autowired
	 ErrorUtil errorUtil;

	@PostMapping("/userregister")
	public ResponseEntity<String> saveUserDetails(@Valid @RequestBody UserRegistrationDTO userRequest) 
	{
		logger.info("inside saveUserDetails");
		return userOperationsService.saveUserDetails(userRequest);
	}
	
	@PostMapping("/checkCredentials")
	public  ResponseEntity<AuthToken> checkCredentials(@Valid @RequestBody UserCredentialsDTO loginTO)  
	{
    	logger.info("inside checkCredentials");
		return userOperationsService.checkLoginCredential(loginTO);
	}
	
	@GetMapping("/{userName}")
	public ResponseEntity<UserDTO> getUserDetails(@PathVariable("userName") String userName) throws ResourceNotFoundException
	{
		logger.info("inside getUserDetails");
		return new ResponseEntity<>(userOperationsService.findByUserName(userName),HttpStatus.OK);
	}
	
	@PostMapping("/searchProducts")
	public ResponseEntity<List<SearchDTO>> getProductDetails(@RequestBody SearchRequest request) 
	{
		logger.info("inside getProductDetails");
		
		
		
		return userOperationsService.findProductDetails(request);
	}
	@PostMapping("/saveProduct")
	public ResponseEntity<?> saveProductOrder(@Valid @RequestBody OrderProductDTO orderProDto)
	{
		return userOperationsService.saveProductOrder(orderProDto);
	}
	
	@PostMapping("/saveCart/{userName}")
	public ResponseEntity<?> saveCart(@PathVariable("userName") String userName,@Valid @RequestBody List<CartDTO> cartDto,BindingResult result)
	{
		if(result.hasErrors())
	     {
	    	 return  errorUtil.getErrorMessages(result);
	     }
		return userOperationsService.saveCart(userName,cartDto);
	}
	
	@GetMapping("/{userName}/{cartId}")
	public ResponseEntity<List<CartDetailsDTO>> getCartDetails(@PathVariable("userName") String userName,@PathVariable("cartId") String cartId) throws CartDetailsNotFoundException
	{
		return userOperationsService.getCartDetails(userName,cartId);
	}
	
	
	@GetMapping("/orderHistory/{userName}")
	public ResponseEntity<List<OrderHistoryDTO>> orderHistory(@PathVariable("userName") String userName)
	{
	
		return userOperationsService.orderHistory(userName);
	}
	
}
