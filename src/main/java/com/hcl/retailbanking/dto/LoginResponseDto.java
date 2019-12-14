package com.hcl.retailbanking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
	private Integer userId;
	private String role;
}
