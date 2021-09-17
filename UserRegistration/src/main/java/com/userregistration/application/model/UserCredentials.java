package com.userregistration.application.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_credentials")
public class UserCredentials implements Serializable 
{
	private static final long serialVersionUID = 7194933261242012049L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_credentials_id")
	private Long userCredId;
	@Column(name="user_name")
	private String userName;
	@Column(name="user_password")
	private String userPassword;	
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;
	@Column(name = "role", nullable = false)
	private String role;
}
