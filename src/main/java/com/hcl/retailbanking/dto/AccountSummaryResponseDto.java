package com.hcl.retailbanking.dto;

import java.util.List;

import com.hcl.retailbanking.entity.Transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSummaryResponseDto {
	
	List<Transaction> transactions;
	String message;

}
