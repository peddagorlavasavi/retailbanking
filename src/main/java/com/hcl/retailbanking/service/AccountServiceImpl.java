package com.hcl.retailbanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.UserRepository;
import com.hcl.retailbanking.util.StringConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * AccountServiceImpl is the service class which implements the method declared
 * in AccountService
 * 
 * @author Hema This interface is used to generate account for the user once
 *         user is registered
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	UserRepository userRepository;

	/**
	 * generateAccount()
	 * @description
	 * generateAccount is used to create account for the user
	 * @param userId
	 * @return
	 */
	@Override
	public Account generateAccount(Integer userId) {
		log.info("generateAccount is used to create account for the user");
		Account account =null;
		User user = userRepository.findUserByUserId(userId);
		if(user!=null) {
			account = new Account();
			account.setUserId(user.getUserId());
			account.setAccountType(StringConstant.ACCOUNT_TYPE);
			account.setIfscCode(StringConstant.IFSC_CODE);
			account.setBalance(10000.0);
			account=accountRepository.save(account);

		}
		return account;
	}
	
	/**
	 * getAccountDetails()
	 * @description getAccountDetails() meth
	 */
	@Override
	public Account getAccountDetails(Integer userId) {
		// TODO Auto-generated method stub
		return accountRepository.findByUserId(userId);
	}

}
