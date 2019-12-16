package com.hcl.retailbanking.service;

import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;

public interface AccountService {

	public Account generateAccount(Integer userId, String accountType);

	public Account getAccountDetails(Integer userId);

	public Account createMortgageAccount(Integer userId, Mortgage mortgage);

}
