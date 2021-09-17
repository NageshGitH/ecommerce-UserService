package com.userregistration.application.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String categoryName;
	private String categoryDescrption;
	private long productId;
	private String productName;
	private double productPrice;
	private int productQuantity;
	  
}
