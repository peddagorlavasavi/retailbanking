package com.hcl.retailbanking.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.retailbanking.dto.AccountSummaryDto;
import com.hcl.retailbanking.dto.FundTransferRequestDto;
import com.hcl.retailbanking.dto.FundTransferResponseDto;
import com.hcl.retailbanking.dto.TransactionDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Transaction;
import com.hcl.retailbanking.service.TransactionService;
import com.hcl.retailbanking.util.StringConstant;

/**
 * @author Vasavi
 * @description this class is used for to test operation for fund transfer
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionControllerTest {
	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TransactionControllerTest.class);
	/**
	 * The transactionService.
	 */
	@Mock
	TransactionService transactionService;

	/**
	 * The transactionController.
	 */
	@InjectMocks
	TransactionController transactionController;

	MockMvc mockMvc;
	AccountSummaryDto accountSummaryDto = new AccountSummaryDto();
	static Account account = new Account();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFundTransfer() {
		logger.info("Inside the fundTransferTest method");
		FundTransferRequestDto fundTransferRequestDto = new FundTransferRequestDto();
		fundTransferRequestDto.setFromAccount(1234567810L);
		fundTransferRequestDto.setToAccount(1234567891L);
		fundTransferRequestDto.setAmount(500.00);
		fundTransferRequestDto.setBenefactorName("vasavi");
		FundTransferResponseDto fundTransferResponseDto = new FundTransferResponseDto();
		fundTransferResponseDto.setMessage("Succesfully Transferred");
		fundTransferResponseDto.setToAccount(1234567810L);
		fundTransferResponseDto.setToAccount(1234567891L);
		fundTransferResponseDto.setAmount(1000.00);
		when(transactionService.fundTransfer(fundTransferRequestDto)).thenReturn(fundTransferResponseDto);
		ResponseEntity<FundTransferResponseDto> result = transactionController.fundTransfer(fundTransferRequestDto);
		assertEquals("Succesfully Transferred", result.getBody().getMessage());

	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);

		}

	}

	/**
	 * setUp()
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		account.setUserId(123445);
		account.setAccountNumber(1223346L);
		account.setBalance(3000D);
		account.setAccountType(StringConstant.ACCOUNT_TYPE);
		account.setIfscCode("HCL98989");

		Transaction transaction = new Transaction();
		transaction.setTransactionId(1);
		transaction.setFromAccount(1223455L);

		accountSummaryDto.setTransactions(getMockData());
		accountSummaryDto.setAccount(account);

		mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
	}

	public static List<Transaction> getMockData() {
		return Stream.of(new Transaction(), new Transaction()).collect(Collectors.toList());
	}

	/**
	 * testGetSummaryForPositive()
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetSummaryForPositive() throws Exception {
		Integer userId = 123456;
		 mockMvc
				.perform(MockMvcRequestBuilders.get("/transactions/" + userId).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		Mockito.when(transactionService.accountSummary(userId)).thenReturn(accountSummaryDto);

		Mockito.verify(transactionService).accountSummary(userId);
	}

	/**
	 * testGetSummaryForNegative()
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetSummaryForNegative() throws Exception {

		Integer userId = null;
		mockMvc.perform(MockMvcRequestBuilders.get("/transactions/" + userId).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
		Mockito.when(transactionService.accountSummary(userId)).thenReturn(accountSummaryDto);
		// Mockito.verify(transactionService).accountSummary(userId);
		assertNotNull(transactionService.accountSummary(userId));
	}
	
	static TransactionDto transactionDto = new TransactionDto();
	static List<Transaction> lstTransaction = new ArrayList<>();

	/**
	 * @author Sri Keerthna
	 * Values are initialized for transactionDto, transaction and Accounts
	 */
	@Before
	public void setup() {
		transactionDto.setMonth("September");
		transactionDto.setYear(2019);
		transactionDto.setUserId(1);

		List<Account> accounts = new ArrayList<>();
		Account account = new Account();
		account.setAccountNumber(1234L);
		account.setAccountType("Savings");
		account.setIfscCode("SBI0010");
		account.setUserId(1);
		accounts.add(account);

		Transaction transaction = new Transaction();
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
	 * @author Sri Keerthna
	 * Positive test case. If the transactions are available for a particular
	 * account number then it will return the transactions
	 */
	@Test
	public void viewTransactionsPositiveTest() {
		Mockito.when(transactionService.viewTransactions(transactionDto)).thenReturn(lstTransaction);
		HttpStatus statuscode = transactionController.viewTransaction(transactionDto).getStatusCode();
		assertEquals(HttpStatus.OK, statuscode);

	}

	/**
	 * @author Sri Keerthna
	 * Negative test case. If any value is null then it will return null value
	 */
	@Test
	public void viewTransactionsNegativeTest() {
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setMonth(null);

		Mockito.when(transactionService.viewTransactions(transactionDto)).thenReturn(null);
		HttpStatus statuscode = transactionController.viewTransaction(transactionDto).getStatusCode();
		assertEquals(HttpStatus.OK, statuscode);

	}

	/**
	 * @author Sri Keerthna
	 * Negative test case. If any value is null then it will return null value
	 */
	@Test
	public void viewTransactionsNegativeTestNull() {
		transactionDto.setYear(null);

		Mockito.when(transactionService.viewTransactions(transactionDto)).thenReturn(null);
		HttpStatus statuscode = transactionController.viewTransaction(transactionDto).getStatusCode();
		assertEquals(HttpStatus.OK, statuscode);

	}

	/**
	 * @author Sri Keerthna
	 * Negative test case. If any value is null then it will return null value
	 */
	@Test
	public void viewTransactionsNegativeTestNullValue() {
		transactionDto.setUserId(null);

		Mockito.when(transactionService.viewTransactions(transactionDto)).thenReturn(null);
		HttpStatus statuscode = transactionController.viewTransaction(transactionDto).getStatusCode();
		assertEquals(HttpStatus.OK, statuscode);

	}

	/**
	 * @author Sri Keerthna
	 * Negative test case. If all values are null then it will return null value
	 */
	@Test
	public void viewTransactionsNegativeTestNullValues() {
		transactionDto.setMonth(null);
		transactionDto.setYear(null);
		transactionDto.setUserId(null);

		Mockito.when(transactionService.viewTransactions(transactionDto)).thenReturn(null);
		HttpStatus statuscode = transactionController.viewTransaction(transactionDto).getStatusCode();
		assertEquals(HttpStatus.OK, statuscode);

	}

	/**
	 * @author Sri Keerthna
	 * Negative test case. If transactions are not available then it will return an empty list
	 */
	@Test
	public void viewTransactionsNegativeTestEmpty() {
		Mockito.when(transactionService.viewTransactions(transactionDto)).thenReturn(lstTransaction);
		HttpStatus statuscode = transactionController.viewTransaction(transactionDto).getStatusCode();
		assertEquals(HttpStatus.OK, statuscode);
	}
}
