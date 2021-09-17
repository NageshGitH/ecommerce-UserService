package com.userregistration.application.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class User implements Serializable
{
	private static final long serialVersionUID = -263421372731704175L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="user_id")
	private Long userId;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="mobile_no")
	private long mobileNo;
	@Column(name="aardhaar_no")
	private String aadhaarNo;
	@Column(name="pancard_no")
	private String panCardNo;
	@Column(name="email_id")
	private String emailId;
	@Column(name="creation_date")
	private LocalDateTime creationDate;
	
	
}
