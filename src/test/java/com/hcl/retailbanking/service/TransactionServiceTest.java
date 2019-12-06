package com.hcl.retailbanking.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.hcl.retailbanking.dto.TransactionDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Transaction;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.TransactionRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionServiceTest {

	@InjectMocks
	TransactionServiceImpl transactionService;

	@Mock
	TransactionRepository transactionRepository;

	@Mock
	AccountRepository accountRepository;

	static TransactionDto transactionDto = new TransactionDto();
	static List<Transaction> lstTransaction = new ArrayList<>();
	static Transaction transaction = new Transaction();
	static List<Account> accounts = new ArrayList<>();
	static Account account = new Account();
	static LocalDate fromDate = LocalDate.of(2019, 9, 11);
	static LocalDate toDate = LocalDate.of(2019, 9, 19);

	/**
	 * @author Sri Keerthna. 
	 * Values are initialized for transactionDto, transaction and Accounts
	 */
	@Before
	public void setup() {
		transactionDto.setMonth("September");
		transactionDto.setYear(2019);
		transactionDto.setUserId(1);

		account.setAccountNumber(1234L);
		account.setAccountType("Savings");
		account.setIfscCode("SBI0010");
		account.setUserId(1);
		accounts.add(account);

		transaction.setAmount(200D);
		transaction.setBenefactorName("sri");
		transaction.setFromAccount(1234L);
		transaction.setToAccount(9876L);
		transaction.setTransactionDate(LocalDate.of(2019, 9, 2));
		transaction.setTransactionId(1);
		transaction.setTransactionType("Debited");
		lstTransaction.add(transaction);
	}

	/**
	 * @author Sri Keerthna.
	 * Positive test case.
	 * If there is any  available transactions it will display those transactions
	 */
	@Test
	public void viewTransactionPositiveTest() {
		transaction.setTransactionDate(fromDate);
		Mockito.when(accountRepository.findAccountNumberByUserId(1)).thenReturn(account);
		Mockito.when(transactionRepository.getTransactionsBetweenDates(fromDate, toDate, transaction.getFromAccount()))
				.thenReturn(lstTransaction);
		List<Transaction> transactions = transactionService.viewTransactions(transactionDto);
		assertThat(transactions).hasSize(0);
	}

	/**
	 * @author Sri Keerthna.
	 * Negative test case.
	 * If the transactions are not available it will display empty list
	 */
	@Test
	public void viewTransactionNegativeTest() {
		transactionDto.setUserId(2);
		account.setUserId(1);
		Mockito.when(accountRepository.findAccountNumberByUserId(2)).thenReturn(account);
		List<Transaction> transactions = transactionService.viewTransactions(transactionDto);
		assertThat(transactions).hasSize(0);
	}
}
