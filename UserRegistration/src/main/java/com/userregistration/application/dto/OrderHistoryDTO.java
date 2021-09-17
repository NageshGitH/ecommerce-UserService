package com.userregistration.application.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDTO implements Serializable {
	private static final long serialVersionUID = -929651754268912381L;
	private String userName;
	private double totalAmount;
	List<OrderHistoryDetailsDTO> orderDetails = new ArrayList<>();

}
