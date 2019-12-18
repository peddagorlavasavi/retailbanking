package com.hcl.retailbanking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.retailbanking.dto.AccountSummaryDto;
import com.hcl.retailbanking.dto.FundTransferRequestDto;
import com.hcl.retailbanking.dto.FundTransferResponseDto;
import com.hcl.retailbanking.dto.TransactionDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Transaction;
import com.hcl.retailbanking.exception.AccountNotFoundException;
import com.hcl.retailbanking.exception.CommonException;
import com.hcl.retailbanking.exception.MessageCode;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.TransactionRepository;
import com.hcl.retailbanking.util.ApiConstant;
import com.hcl.retailbanking.util.StringConstant;
import com.hcl.retailbanking.util.Utils;

/**
 * The class TransactionServiceImpl.
 * 
 * @author Vasavi
 *
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
	/**
	 * The TransactionRepository methods are autowired with this
	 * TransactionServiceImpl..
	 */
	@Autowired
	TransactionRepository transactionRepository;

	/**
	 * The AccountRepository methods are autowired with this TransactionServiceImpl
	 */
	@Autowired
	AccountRepository accountRepository;

	/**
	 * 
	 * @description this method is used to do fund transfer in the application
	 * @param fundTransferRequestDTO the fundTransferRequestDTO which contains
	 *                               fromAccount,toAccount,amount,transactionType
	 *                               and benefactorName
	 * @return fundTransferResponseDto
	 */
	@Override
	public FundTransferResponseDto fundTransfer(FundTransferRequestDto fundTransferRequestDTO) {
		logger.info("Inside fundTransfer method");
		Account account1 = null, account2 = null;

		Optional<Account> fromAccount = accountRepository.findById(fundTransferRequestDTO.getFromAccount());
		Optional<Account> toAccount = accountRepository.findById(fundTransferRequestDTO.getToAccount());

		// checking source and destination accounts existed or not
		if (!fromAccount.isPresent() || !toAccount.isPresent()) {
			throw new CommonException(MessageCode.ACCOUNT_INVALID);
		}
		// checking from source account to source account we can not do fund
		// transactions
		if (fundTransferRequestDTO.getFromAccount().equals(fundTransferRequestDTO.getToAccount())) {
			throw new CommonException(MessageCode.SAME_ACCOUNT_INVALID);
		}

		// source account should be maintain with the minimal balance
		if (!fromAccountBalanceVerification(fromAccount.get(), fundTransferRequestDTO)) {
			throw new CommonException(MessageCode.insufficentFunds(fromAccount.get().getBalance()));
		}

		if (fromAccount.isPresent()) {
			account1 = fromAccount.get();
			account1.setBalance(fromAccount.get().getBalance() - fundTransferRequestDTO.getAmount());
		}
		if (toAccount.isPresent()) {
			account2 = toAccount.get();
			account2.setBalance(toAccount.get().getBalance() + fundTransferRequestDTO.getAmount());
		}

		Transaction transaction = debit(fundTransferRequestDTO, account1);
		if (transaction != null)
			credit(fundTransferRequestDTO, account2);

		return new FundTransferResponseDto("Successfully Transferred", fundTransferRequestDTO.getFromAccount(),
				fundTransferRequestDTO.getToAccount(), fundTransferRequestDTO.getAmount());
	}

	/**
	 * @author Sujal
	 * @description This method is used to debit money in an account
	 * @param fundTransferRequestDTO
	 * @param account                in which the amount will be debited
	 * @return transaction object
	 */
	@Transactional
	private synchronized Transaction debit(FundTransferRequestDto fundTransferRequestDTO, Account account) {

		Transaction transaction1 = getTransactionObject(fundTransferRequestDTO, StringConstant.DEBIT);

		transaction1 = transactionRepository.save(transaction1);
		if (transaction1 != null)
			accountRepository.save(account);

		return transaction1;
	}

	/**
	 * @author Sujal
	 * @description This method is used to credit money in an account
	 * @param fundTransferRequestDTO
	 * @param account                in which the amount will be created
	 * @return transaction object
	 */
	@Transactional
	private synchronized Transaction credit(FundTransferRequestDto fundTransferRequestDTO, Account account) {

		Long fromAccountNumber = fundTransferRequestDTO.getFromAccount();
		Long toAccountNumber = fundTransferRequestDTO.getToAccount();

		fundTransferRequestDTO.setFromAccount(toAccountNumber);
		fundTransferRequestDTO.setToAccount(fromAccountNumber);

		Transaction transaction2 = getTransactionObject(fundTransferRequestDTO, StringConstant.CREDIT);
		transaction2 = transactionRepository.save(transaction2);
		if (transaction2 != null)
			accountRepository.save(account);

		return transaction2;
	}

	/**
	 * @description Creating transaction object with fundTransferRequestDTO
	 * @param fundTransferRequestDTO
	 * @param transactionType
	 * @return
	 */
	private Transaction getTransactionObject(FundTransferRequestDto fundTransferRequestDTO, String transactionType) {
		Transaction transaction = new Transaction();
		transaction.setFromAccount(fundTransferRequestDTO.getFromAccount());
		transaction.setToAccount(fundTransferRequestDTO.getToAccount());
		transaction.setAmount(fundTransferRequestDTO.getAmount());
		transaction.setBenefactorName(fundTransferRequestDTO.getBenefactorName());
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionType(transactionType);
		transaction.setRemarks(fundTransferRequestDTO.getRemarks());
		return transaction;
	}

	/**
	 * @description validation to create account check the balance in from account
	 * 
	 * @param account
	 * @param fundTransferRequestDTO
	 * @return
	 */
	private boolean fromAccountBalanceVerification(Account account, FundTransferRequestDto fundTransferRequestDTO) {
		if ((account.getBalance() - fundTransferRequestDTO.getAmount()) < 1000) {
			return false;
		}
		return true;

	}

	/**
	 * @author Sujal
	 * @description this method is used to do view account Summary in the
	 *              application accountSummary() method will have account details
	 *              and last five transaction
	 * @param userId
	 * @return accountSummaryDto
	 */
	public List<AccountSummaryDto> accountSummary(Integer userId) {
		List<AccountSummaryDto> list = new ArrayList<AccountSummaryDto>();
		// Account account = accountRepository.findByUserId(userId);
		Account account = accountRepository.getAccountByUserIdAndAccountType(userId,
				StringConstant.SAVINGS_ACCOUNT_TYPE);

		Account account1 = accountRepository.getAccountByUserIdAndAccountType(userId,
				StringConstant.MORTGAGE_ACCOUNT_TYPE);

		if (account != null) {
			AccountSummaryDto accountSummaryDto = new AccountSummaryDto();

			accountSummaryDto.setAccount(account);
			List<Transaction> transactionList = transactionRepository
					.findTop5ByFromAccountOrderByTransactionIdDesc(account.getAccountNumber());
			if (transactionList != null && !transactionList.isEmpty()) {
				accountSummaryDto.setMessage(ApiConstant.SUCCESS);
				accountSummaryDto.setTransactions(transactionList);
			} else {
				accountSummaryDto.setMessage(ApiConstant.EMPTY);
				accountSummaryDto.setTransactions(Collections.emptyList());
			}
			list.add(accountSummaryDto);
		} else {
			throw new AccountNotFoundException();
		}
		if (account1 != null) {
			AccountSummaryDto accountSummaryDto = new AccountSummaryDto();
			accountSummaryDto.setAccount(account1);
			List<Transaction> transactionList = transactionRepository
					.findTop5ByFromAccountOrderByTransactionIdDesc(account1.getAccountNumber());
			if (transactionList != null && !transactionList.isEmpty()) {
				accountSummaryDto.setMessage(ApiConstant.SUCCESS);
				accountSummaryDto.setTransactions(transactionList);
			} else {
				accountSummaryDto.setMessage(ApiConstant.EMPTY);
				accountSummaryDto.setTransactions(Collections.emptyList());
			}
			list.add(accountSummaryDto);
		}

		return list;
	}

	/**
	 * @author Sri Keerthna In this method input is taken from user and using their
	 *         account number monthly transactions will be displayed.
	 * @param transactionDto
	 * @return
	 */
	@Override
	public List<Transaction> viewTransactions(TransactionDto transactionDto) {
		logger.info("Entering into transaction method with inputs");
		String month = transactionDto.getMonth();
		Integer year = transactionDto.getYear();
		Account account = accountRepository.getAccountByUserIdAndAccountType(transactionDto.getUserId(), transactionDto.getType());
		Long accountNumber = account.getAccountNumber();
		if (accountNumber != null) {
			List<LocalDate> dates = Utils.getDateFromMonthAndYear(month, year);
			if (!dates.isEmpty()) {
				LocalDate fromDate = dates.get(0);
				LocalDate toDate = dates.get(1);
				return transactionRepository.getTransactionsBetweenDates(fromDate, toDate, accountNumber);
			} else {
				return Collections.emptyList();
			}
		} else {
			return Collections.emptyList();
		}
	}
}
