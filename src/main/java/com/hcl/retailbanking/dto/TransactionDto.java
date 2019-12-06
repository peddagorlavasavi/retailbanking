package com.hcl.retailbanking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDto {

	private Integer userId;
	private String month;
	private Integer year;
}
