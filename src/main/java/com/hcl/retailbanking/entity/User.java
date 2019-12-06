package com.hcl.retailbanking.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * User is the entity class
 * 
 * @author Hema This class will show the details that should be entered by the
 *         user while registering
 */

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String mobileNumber;
	private String email;
	private String typeOfId;
	private String idProofNumber;
	private Integer age;
	private String address;
	private String password;
	private String gender;
	private String status;
}
