package com.hcl.retailbanking.dto;

import java.io.Serializable;

import com.hcl.retailbanking.entity.Mortgage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long accountNumber;
	private Integer userId;
	private Double balance;
	private String accountType;
	private Integer age;
	private String firstName;
	private Double salary;
	private String gender;

	private Mortgage mortgage;
}
