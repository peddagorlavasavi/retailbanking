package com.hcl.retailbanking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
	private Integer userId;
	private Long accountNumber;
	private String userName;
	private String status;
}
