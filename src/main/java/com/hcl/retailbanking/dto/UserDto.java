package com.hcl.retailbanking.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.hcl.retailbanking.util.StringConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * UserDto is the dto class
 * 
 * @author Hema This class is used to get details of the user that to be
 *         registered
 */

@Getter
@Setter
public class UserDto {

	@NotBlank(message = StringConstant.FIRSTNAME_MANDATORY)
	private String firstName;

	@NotBlank(message = StringConstant.LASTNAME_MANDATORY)
	private String lastName;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dob;

	@NotBlank(message = StringConstant.MOBILENUMBER_MANDATORY)
	@Pattern(regexp = "(^$|[0-9]{10})", message = StringConstant.MOBILENUMBER_VALID)
	private String mobileNumber;

	@Email(message = StringConstant.EMAIL_VALID)
	private String email;

	private String typeOfId;
	private String idProofNumber;

	private Integer age;
	private String address;

	@NotBlank(message = StringConstant.PASSWORD_MANDATORY)
	private String password;

	private String gender;

}
