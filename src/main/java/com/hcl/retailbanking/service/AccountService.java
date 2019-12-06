package com.hcl.retailbanking.service;

import com.hcl.retailbanking.entity.Account;

public interface AccountService {

	public Account generateAccount(Integer userId);

	public Account getAccountDetails(Integer userId);

}
