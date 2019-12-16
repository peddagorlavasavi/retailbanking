package com.hcl.retailbanking.service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.entity.Transaction;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.MortgageRepository;
import com.hcl.retailbanking.repository.TransactionRepository;
import com.hcl.retailbanking.repository.UserRepository;
import com.hcl.retailbanking.util.AccountValidator;
import com.hcl.retailbanking.util.StringConstant;

/**
 * AccountServiceImpl is the service class which implements the method declared
 * in AccountService
 * 
 * @author Hema This interface is used to generate account for the user once
 *         user is registered
 */
@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MortgageRepository mortgageRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Qualifier(value = "accountValidator")
	@Autowired
	AccountValidator<Mortgage> accountValidator;

	/**
	 * generateAccount()
	 * 
	 * @description generateAccount is used to create account for the user
	 * @param userId
	 * @return
	 */
	@Override
	public Account generateAccount(Integer userId, String type) {
		logger.info("generateAccount is used to create account for the user");
		Account account = null;
		User user = userRepository.findUserByUserId(userId);
		if (user != null) {
			account = new Account();
			account.setAccountType(type);
			account.setUserId(user.getUserId());
			account.setIfscCode(StringConstant.IFSC_CODE);
			account.setBalance(10000.00);
			account = accountRepository.save(account);

		}
		return account;
	}

	/**
	 * getAccountDetails()
	 * 
	 * @description getAccountDetails() method used for getting account details
	 */
	@Override
	public Account getAccountDetails(Integer userId) {
		// TODO Auto-generated method stub
		return accountRepository.getAccountByUserIdAndAccountType(userId, StringConstant.SAVINGS_ACCOUNT_TYPE);
	}

	/**
	 * @author Sujal
	 * @description This method is used to create a mortgage account for a saving
	 *              account
	 * @param userId   of admin
	 * @param mortgage details
	 * @return account info of the created mortgage account
	 */
	@Override
	public Account createMortgageAccount(Integer userId, Mortgage mortgage) {
		Account account = null;
		Mortgage mortgage1 = null;

		User user = userRepository.findUserByUserId(userId);
		User customer = userRepository.findUserByUserId(mortgage.getCustomerId());

		if (user != null && customer != null && user.getRole().equalsIgnoreCase(StringConstant.ADMIN_ROLE)) {
			logger.info(user.getRole());
			if (accountValidator.validate(mortgage)) {
				logger.info("INSIDE CREATE ACCOUNT ");

				account = new Account();
				account.setAccountType(StringConstant.MORTGAGE_ACCOUNT_TYPE);
				account = generateAccount(customer.getUserId(), StringConstant.MORTGAGE_ACCOUNT_TYPE);
				mortgage1 = createMortgage(mortgage, account);
				if (mortgage1 != null)
					creditAmount(mortgage1, account, user);
			}

		}
		return account;
	}

	/**
	 * @author Sujal
	 * @description This method is used to save the mortgage information
	 * @param mortgage
	 * @param account
	 * @return mortgage details
	 */
	@Transactional
	private Mortgage createMortgage(Mortgage mortgage, Account account) {

		logger.info("inside create  mortgage ");
		mortgage.setAccount(account);
		return mortgageRepository.save(mortgage);
	}

	/**
	 * @author Sujal
	 * @description This method is used to perform credit the mortgage amount in the
	 *              customer's saving account
	 * @param mortgage is the mortgage information
	 * @param account  will be created for the saving account
	 * @param user     is the admin who perform this operation
	 * @return transaction for the mortgage amount
	 */
	@Transactional
	private synchronized Transaction creditAmount(Mortgage mortgage, Account account, User user) {
		Transaction transaction2 = null;
		Account account1 = accountRepository.getAccountByUserIdAndAccountType(user.getUserId(),
				StringConstant.SAVINGS_ACCOUNT_TYPE);
		if (account1 != null) {
			Transaction transaction = new Transaction();
			transaction.setAmount(mortgage.getAmount());
			transaction.setBenefactorName(user.getFirstName());
			transaction.setToAccount(account1.getAccountNumber());
			transaction.setFromAccount(account.getAccountNumber());
			transaction.setTransactionType(StringConstant.CREDIT);
			transaction.setTransactionDate(LocalDate.now());
			transaction2 = transactionRepository.save(transaction);
			if (transaction2 != null) {
				Double balance = account.getBalance();
				balance = balance - mortgage.getAmount();
				account.setBalance(balance);
				accountRepository.save(account);

				Double balance1 = account1.getBalance();
				balance1 = balance1 + mortgage.getAmount();
				account1.setBalance(balance1);
				accountRepository.save(account1);
			}
		}

		return transaction2;
	}

}
