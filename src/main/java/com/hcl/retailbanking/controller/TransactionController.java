package com.hcl.retailbanking.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.retailbanking.dto.AccountSummaryDto;
import com.hcl.retailbanking.dto.AccountSummaryResponseDto;
import com.hcl.retailbanking.dto.FundTransferRequestDto;
import com.hcl.retailbanking.dto.FundTransferResponseDto;
import com.hcl.retailbanking.dto.TransactionDto;
import com.hcl.retailbanking.entity.Transaction;
import com.hcl.retailbanking.exception.CommonException;
import com.hcl.retailbanking.service.TransactionService;

/**
 * @author Vasavi
 *
 */
@RestController
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
@RequestMapping("/transactions")
public class TransactionController {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	/**
	 * The transactionServiceImpl.
	 */
	@Autowired
	TransactionService transactionService;

	/**
	 * 
	 * @description this method is used to do fund transfer in the application
	 * @param fundTransferRequestDto the fundTransferRequestDto which contains
	 *                               fromAccount,toAccount,amount,transactionType
	 *                               and benefactorName
	 * @return fundTransferResponseDto
	 * @throws CommonException
	 */
	@PostMapping("/fundTransfer")
	public ResponseEntity<FundTransferResponseDto> fundTransfer(
			@RequestBody FundTransferRequestDto fundTransferRequestDto) throws CommonException {
		logger.debug("In TransactionController:fundTransfer");
		FundTransferResponseDto fundTransferResponseDto = transactionService.fundTransfer(fundTransferRequestDto);
		return new ResponseEntity<>(fundTransferResponseDto, HttpStatus.OK);
	}
	
	/**
	 * @description this method is used to view account summary
	 *  accountSummary() method will return account summary as well as last five transactions
	 * @param userId
	 * @return
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<AccountSummaryDto> accountSummary(@PathVariable("userId") Integer userId) {
		logger.debug("In TransactionController:fundTransfer");
		AccountSummaryDto accountSummaryDto = transactionService.accountSummary(userId);
		return new ResponseEntity<>(accountSummaryDto, HttpStatus.OK);
	}

		/**
		 * @author Sri Keerthna. Transactions are viewed for a particular user in a
		 *         monthly based.
		 * @param transactionDto
		 * @return
		 */
		@PostMapping("/monthTransaction")
		public ResponseEntity<AccountSummaryResponseDto> viewTransaction(@RequestBody TransactionDto transactionDto) {
			AccountSummaryResponseDto apiResponseDto = new AccountSummaryResponseDto();
			
					
			if (transactionDto.getMonth() != null && transactionDto.getYear()!=null
					&& transactionDto.getUserId() != null) {

				List<Transaction> transactions = transactionService.viewTransactions(transactionDto);
				if (transactions.isEmpty()) {
					logger.debug("Result has a empty set");
					apiResponseDto.setMessage("No transactions available");
					return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
				} else {
					apiResponseDto.setMessage("Monthly transactions");
					apiResponseDto.setTransactions(transactions);
					return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
				}
			} else {
				logger.debug("Data entered has a null value");
				apiResponseDto.setMessage("Invalid input data");
				return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
			}
		}

	}


