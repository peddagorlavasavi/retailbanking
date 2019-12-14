package com.hcl.retailbanking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.retailbanking.dto.MortgageResponseDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.service.AccountService;
import com.hcl.retailbanking.util.ApiConstant;

@RestController
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@PostMapping("/mortgage/{userId}")
	public ResponseEntity<MortgageResponseDto> createMortgageAccount(@PathVariable("userId") Integer userId, @RequestBody Mortgage mortgage) {
		MortgageResponseDto mortgageResponseDto = new MortgageResponseDto();
		
		Account account =accountService.createMortgageAccount(userId, mortgage);
		if(account!=null) {
			mortgageResponseDto.setAccountNumber(account.getAccountNumber());
			mortgageResponseDto.setMessage(ApiConstant.CREATE_SUCCESS);
			return ResponseEntity.ok(mortgageResponseDto);
		}else {
			mortgageResponseDto.setMessage(ApiConstant.CREATE_FAILED);
		}
		
		return ResponseEntity.ok(mortgageResponseDto);
	
	}
}
