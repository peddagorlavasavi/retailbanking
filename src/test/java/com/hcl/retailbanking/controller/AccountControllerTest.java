package com.hcl.retailbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.hcl.retailbanking.dto.MortgageResponseDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.service.AccountService;
import com.hcl.retailbanking.util.ApiConstant;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountControllerTest {

	@InjectMocks
	AccountController accountController;

	@Mock
	AccountService accountService;

	static MortgageResponseDto mortgageResponseDto = new MortgageResponseDto();
	static Mortgage mortgage = new Mortgage();
	static Account account = new Account();
	static User user = new User();

	@Before
	public void setUp() {
		user.setUserId(1);
		mortgage.setMortgageId(1);
		account.setUserId(1);
		account.setAccountNumber(12345L);
		mortgageResponseDto.setAccountNumber(account.getAccountNumber());
		mortgageResponseDto.setMessage(ApiConstant.CREATE_SUCCESS);
	}

	@Test
	public void testCreateMortgageAccount() {
		Mockito.when(accountService.createMortgageAccount(1, mortgage)).thenReturn(account);
		ResponseEntity<MortgageResponseDto> mortgageResponseDto = accountController.createMortgageAccount(1, mortgage);
		assertEquals(ApiConstant.CREATE_SUCCESS, mortgageResponseDto.getBody().getMessage());
	}

	@Test
	public void testCreateMortgageAccountNegative() {
		Account account = null;
		mortgageResponseDto.setMessage(ApiConstant.CREATE_FAILED);
		Mockito.when(accountService.createMortgageAccount(2, mortgage)).thenReturn(account);
		ResponseEntity<MortgageResponseDto> mortgageResponseDto = accountController.createMortgageAccount(2, mortgage);
		assertEquals(ApiConstant.CREATE_FAILED, mortgageResponseDto.getBody().getMessage());
	}

}
