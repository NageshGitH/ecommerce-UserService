package com.userregistration.application.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.userregistration.application.dto.CartDTO;
import com.userregistration.application.dto.CartDetailsDTO;
import com.userregistration.application.dto.OrderHistoryDTO;
import com.userregistration.application.dto.OrderProductDTO;
import com.userregistration.application.dto.SearchDTO;
import com.userregistration.application.dto.SearchRequest;
import com.userregistration.application.exception.InSufficientBalanceException;
import com.userregistration.application.exception.ResourceNotFoundException;

@FeignClient(name="http://ORDER-SERVICE/api/orderservice")
public interface OrderServiceInterface
{
	
	@PostMapping("/products/searchproducts")
	public ResponseEntity<List<SearchDTO>> getCategoryProductDetails(@RequestBody SearchRequest request);
	
	@PostMapping("/")
	public ResponseEntity<String> productOrder(@RequestBody OrderProductDTO orderProDto)  throws InSufficientBalanceException, ResourceNotFoundException;
	
	@GetMapping("/orderHistory/{userName}/{userId}")
	public ResponseEntity<List<OrderHistoryDTO>> orderHistory(@PathVariable("userName") String userName,@PathVariable("userId") String userId);
	
	@PostMapping("/addCart/{userId}")
	public ResponseEntity<String> saveCartDetails(@PathVariable("userId") String userId,@RequestBody List<CartDTO> cartDto);  
	
	
	@GetMapping("/{userId}/{cartId}")
	public ResponseEntity<List<CartDetailsDTO>> getCartDetails(@PathVariable("userId") String userId,@PathVariable("cartId") String cartId);  
	
}	
