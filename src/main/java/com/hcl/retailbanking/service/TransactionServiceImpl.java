package com.hcl.retailbanking.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
	 * The TransactionRepository methods are autowired with this TransactionServiceImpl..
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
		Optional<Account> fromAccount = accountRepository.findById(fundTransferRequestDTO.getFromAccount());
		Optional<Account> toAccount = accountRepository.findById(fundTransferRequestDTO.getToAccount());

		// checking from source account to destination account we can not send negative
		// and zero balance
		if (fundTransferRequestDTO.getAmount() <= 0) {
			throw new CommonException(MessageCode.AMMOUNT_INVALID);
		}
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

		fromAccount.get().setBalance(fromAccount.get().getBalance() - fundTransferRequestDTO.getAmount());
		toAccount.get().setBalance(toAccount.get().getBalance() + fundTransferRequestDTO.getAmount());

		Transaction transaction1 = getTransactionObject(fundTransferRequestDTO, StringConstant.DEBIT);
		Long fromAccountNumber = fundTransferRequestDTO.getFromAccount();
		Long toAccountNumber = fundTransferRequestDTO.getToAccount();

		fundTransferRequestDTO.setFromAccount(toAccountNumber);
		fundTransferRequestDTO.setToAccount(fromAccountNumber);

		Transaction transaction2 = getTransactionObject(fundTransferRequestDTO, StringConstant.CREDIT);
		transactionRepository.save(transaction1);
		transactionRepository.save(transaction2);
		accountRepository.save(fromAccount.get());
		accountRepository.save(toAccount.get());
		return new FundTransferResponseDto("Successfully Transferred", fundTransferRequestDTO.getFromAccount(),
				fundTransferRequestDTO.getToAccount(), fundTransferRequestDTO.getAmount());
	}

	/**
	 * getTransactionObject()
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
		return transaction;
	}

	/**
	 * fromAccountBalanceVerification()
	 * @description validation to create account
	 * check the balance in from account
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
	 * @description this method is used to do view account Summary in the
	 *              application accountSummary() method will have account details
	 *              and last five transaction
	 * @param userId
	 * @return accountSummaryDto
	 */
	public AccountSummaryDto accountSummary(Integer userId) {
		AccountSummaryDto accountSummaryDto = new AccountSummaryDto();
		Account account = accountRepository.findByUserId(userId);

		if (account != null) {
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
		} else {
			throw new AccountNotFoundException();
		}
		return accountSummaryDto;
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
		Account account = accountRepository.findAccountNumberByUserId(transactionDto.getUserId());
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
