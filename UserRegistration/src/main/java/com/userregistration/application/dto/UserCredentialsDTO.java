package com.userregistration.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsDTO 
{
	private static final long serialVersionUID = 1L;
	private String userName;
	private String userPassword;

}
