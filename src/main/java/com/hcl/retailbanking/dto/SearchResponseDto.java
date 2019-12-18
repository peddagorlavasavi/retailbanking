package com.hcl.retailbanking.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.hcl.retailbanking.entity.Mortgage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponseDto implements Serializable {

	
	private static final long serialVersionUID = 1L;
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
	private String role;

	private Mortgage mortgage;
}
