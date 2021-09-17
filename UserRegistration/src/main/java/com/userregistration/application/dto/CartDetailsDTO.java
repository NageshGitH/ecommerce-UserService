package com.userregistration.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailsDTO 
{
  private String ProductName;
  private String quantity;
  private String price;
  
}
