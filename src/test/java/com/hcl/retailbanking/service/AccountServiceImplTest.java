package com.hcl.retailbanking.service;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.entity.Transaction;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.MortgageRepository;
import com.hcl.retailbanking.repository.TransactionRepository;
import com.hcl.retailbanking.repository.UserRepository;
import com.hcl.retailbanking.util.StringConstant;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountServiceImplTest {

	@InjectMocks
	AccountServiceImpl AccountService;

	@Mock
	AccountRepository accountRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	TransactionRepository transactionRepository;

	@Mock
	MortgageRepository mortgageRepository;

	static Transaction transaction1 = null;
	static Transaction transaction = new Transaction();
	static Mortgage mortgage = new Mortgage();
	static Account account = new Account();
	static Account account1 = new Account();

	@Before
	public void setup() {
		User user = new User();
		user.setUserId(1);
		user.setStatus("Savings");
		user.setFirstName("sri");
		mortgage.setAmount(20000D);

		transaction.setAmount(mortgage.getAmount());
		transaction.setBenefactorName(user.getFirstName());
		transaction.setToAccount(account.getAccountNumber());
		transaction.setFromAccount(account1.getAccountNumber());
		transaction.setTransactionType(StringConstant.CREDIT);
		transaction.setTransactionDate(LocalDate.now());

		account1.setAccountNumber(234567L);
		account.setAccountNumber(123456L);
		account.setAccountType(StringConstant.SAVINGS_ACCOUNT_TYPE);
		account.setBalance(20000D);
		account.setIfscCode("IDB1000");
		account.setUserId(1);
	}

	@Test
	public void creditAmountPositiveTest() {
		Account account = null;
		Mockito.when(accountRepository.getAccountByUserIdAndAccountType(1, StringConstant.SAVINGS_ACCOUNT_TYPE))
				.thenReturn(account);
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction1);
	}
}
