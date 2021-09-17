package com.userregistration.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.userregistration.application.model.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
	
	@Query("select c.panCardNo, c.aadhaarNo ,  c.emailId from User c where  c.panCardNo=:panNo or c.aadhaarNo=:aadhaarNo"
			+ " or c.emailId =:emailId")
	Optional<String> validateCustomer(String panNo,String aadhaarNo,String emailId);

}
