package com.userregistration.application.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDetailsDTO implements Serializable
{
	private static final long serialVersionUID = -9187738826525221591L;
	
	private String productName; 
	private double price;
	private int quantity;
	private double totalPrice;
	private String orderDate;

}
