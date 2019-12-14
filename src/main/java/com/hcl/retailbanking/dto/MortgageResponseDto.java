package com.hcl.retailbanking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MortgageResponseDto {
	
	private Long accountNumber;
	private Integer customerId;
	private String message;
	private Integer statusCode;
}
