package com.hcl.retailbanking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.retailbanking.dto.FundTransferRequestDto;
import com.hcl.retailbanking.dto.FundTransferResponseDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Transaction;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.TransactionRepository;
import com.hcl.retailbanking.util.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Vasavi
 * @description this class is used for to test operation for fund transfer
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {
	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImplTest.class);
	/**
	 * The transactionService.
	 */
	@InjectMocks
	TransactionServiceImpl transactionService;
	/**
	 * The transactionRespository.
	 */
	@Mock
	TransactionRepository transactionRespository;
	@Mock
	AccountRepository accountRepository;
	Transaction transaction;
	static Transaction transaction1;
	static Transaction transaction2;
	FundTransferRequestDto fundTransferRequestDto;
	FundTransferResponseDto fundTransferResponseDto;
	FundTransferRequestDto fundTransferRequestDtos;
	Account account;

	public FundTransferRequestDto getFundTransferRequestDto() {
		fundTransferRequestDto = new FundTransferRequestDto();
		fundTransferRequestDto.setFromAccount(1234567810L);
		fundTransferRequestDto.setToAccount(1234567891L);
		fundTransferRequestDto.setAmount(1000.00);
		fundTransferRequestDto.setBenefactorName("vasavi");
		return fundTransferRequestDto;
	}

	public FundTransferResponseDto getFundTransferResponseDto() {
		fundTransferResponseDto = new FundTransferResponseDto();
		fundTransferResponseDto.setMessage("Successfully Transfered");
		fundTransferResponseDto.setFromAccount(1234567810L);
		fundTransferResponseDto.setToAccount(1234567891L);
		fundTransferResponseDto.setAmount(5000.00);
		return fundTransferResponseDto;
	}

	public Account getAccount() {
		account = new Account();
		account.setAccountNumber(123458910L);
		account.setAccountType("credit");
		account.setBalance(2000.00);
		account.setIfscCode("ICICI000111");
		account.setUserId(8);
		return account;
	}

	public Transaction getTransaction() {
		transaction = new Transaction();
		transaction.setFromAccount(1234567810L);
		transaction.setToAccount(1234567891L);
		transaction.setTransactionId(1);
		transaction.setAmount(1000.00);
		transaction.setBenefactorName("Vasavi");
		transaction.setTransactionType("debit");
		return transaction;
	}

	public Transaction getTransaction1() {
		transaction1 = new Transaction();
		transaction1.setTransactionId(1);
		transaction1.setFromAccount(1223346L);
		transaction1.setToAccount(232323L);
		transaction1.setAmount(3000D);
		transaction1.setTransactionDate(Utils.getCurrentDate());
		transaction1.setBenefactorName("Ronaldo");
		return transaction1;
	}

	public Transaction getTransaction2() {
		transaction2 = new Transaction();
		transaction2.setTransactionId(1);
		transaction2.setFromAccount(333333L);
		transaction2.setToAccount(432211L);
		transaction2.setAmount(2400D);
		transaction2.setTransactionDate(Utils.getCurrentDate());
		transaction2.setBenefactorName("Messi");
		return transaction2;
	}

	// negative test case for zero balance
	public FundTransferRequestDto getFundTransferRequestDtos() {
		fundTransferRequestDtos = new FundTransferRequestDto();
		fundTransferRequestDtos.setFromAccount(1234567810L);
		fundTransferRequestDtos.setToAccount(1234567891L);
		fundTransferRequestDtos.setAmount(0.00);
		fundTransferRequestDtos.setBenefactorName("vasavi");
		return fundTransferRequestDtos;
	}

	@Before
	public void setup() {
		transaction = getTransaction();
		transaction1 = getTransaction1();
		transaction2 = getTransaction2();
		fundTransferRequestDto = getFundTransferRequestDto();
		fundTransferResponseDto = getFundTransferResponseDto();
		fundTransferRequestDtos = getFundTransferRequestDto();
		account = getAccount();

	}

	@Test
	public void testFundTransfer() {
		logger.info("Inside the fundTransferTest method ");
		Mockito.when(accountRepository.findById(fundTransferRequestDto.getFromAccount()))
				.thenReturn(Optional.of(account));
		Mockito.when(accountRepository.findById(fundTransferRequestDto.getToAccount()))
				.thenReturn(Optional.of(account));
		FundTransferResponseDto fundTransferResponseDto = transactionService.fundTransfer(fundTransferRequestDto);
		assertEquals("Successfully Transferred", fundTransferResponseDto.getMessage());
	}

	// checking for zero amount fund transfer test case
	@Test
	public void testFundTransferNegative() {
		logger.info("Inside the fundTransferNegativeTest method ");
		Mockito.when(accountRepository.findById(fundTransferRequestDto.getFromAccount()))
				.thenReturn(Optional.of(account));
		Mockito.when(accountRepository.findById(fundTransferRequestDto.getToAccount()))
				.thenReturn(Optional.of(account));
		FundTransferResponseDto fundTransferResponseDtos = transactionService.fundTransfer(fundTransferRequestDtos);
		assertEquals("Successfully Transferred", fundTransferResponseDtos.getMessage());

	}

	/**
	 * testGetSummaryForPositive()
	 */
	@Test
	public void testGetSummaryForPositive() {
		//Integer userId = 123456;
		Mockito.when(accountRepository.findByUserId(account.getUserId())).thenReturn(account);
		List<Transaction> transactionList =  transactionRespository.findTop5ByFromAccountOrderByTransactionIdDesc(account.getAccountNumber());
		Mockito.when(transactionList).thenReturn(getMockData());
		assertNotNull(account);
		assertThat(account.getUserId()).isEqualToIgnoringNullFields(account.getUserId());
		assertNotNull(transactionList);
		transactionList.forEach(transaction -> {
			assertThat(transaction).isNotNull();
			assertNotNull(transaction.getTransactionId());
			assertNotNull(transaction.getFromAccount());
			assertNotNull(transaction.getToAccount());
			assertNotNull(transaction.getBenefactorName());
			assertNotNull(transaction.getAmount());
			assertNotNull(transaction.getTransactionDate());
		});
	}

	public static List<Transaction> getMockData() {
		return Stream.of(transaction1, transaction2).collect(Collectors.toList());
	}

	/**
	 * testGetSummaryForNegative()
	 */
	@Test
	public void testGetSummaryForNegative() {
		Integer userId = null;
		Mockito.when(accountRepository.findByUserId(userId)).thenReturn(null);
		List<Transaction> transactionList = transactionRespository
				.findTop5ByFromAccountOrderByTransactionIdDesc(000L);
		Mockito.when(transactionList).thenReturn(Collections.emptyList());
		assertNull(accountRepository.findByUserId(userId));
		assertThat(transactionList).hasSize(0);
	}
}
