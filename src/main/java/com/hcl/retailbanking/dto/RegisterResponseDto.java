package com.hcl.retailbanking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDto { 
	private String message;
	private Long accountNumber;
	private Double balance;

}
