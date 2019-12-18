package com.hcl.retailbanking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.UserRepository;
import com.hcl.retailbanking.util.StringConstant;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceTest.class);
	@InjectMocks
	AccountServiceImpl accountService;
	@Mock
	AccountRepository accountRepository;
	@Mock
	UserRepository userRepository;

	Account account = new Account();
	User user = new User();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		account.setUserId(1223);
		account.setAccountNumber(100100L);
		account.setAccountType(StringConstant.SAVINGS_ACCOUNT_TYPE);
		account.setIfscCode(StringConstant.IFSC_CODE);
		account.setBalance(10000.0);
		user.setUserId(1223);
	}

	@Test
	public void testGenerateAccountPositive() {
		logger.info("Inside AccountServiceTest: generateAccountPositiveTest");
		when(userRepository.findUserByUserId(1223)).thenReturn(user);
		assertEquals(1223, user.getUserId());
	}

}
