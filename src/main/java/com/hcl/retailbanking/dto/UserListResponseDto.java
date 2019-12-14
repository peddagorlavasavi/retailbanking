package com.hcl.retailbanking.dto;

import com.hcl.retailbanking.entity.Mortgage;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserListResponseDto {
	
	private Long accountNumber;
	private Integer userId;
	private Double balance;
	private String accountType;
	private Integer age;
	private String firstName;
	private Double salary;
	private String gender;
	
	public Mortgage mortgage;
}
