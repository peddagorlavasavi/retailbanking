package com.hcl.retailbanking.service;

import java.util.List;

import com.hcl.retailbanking.dto.AccountSummaryDto;
import com.hcl.retailbanking.dto.FundTransferRequestDto;
import com.hcl.retailbanking.dto.FundTransferResponseDto;
import com.hcl.retailbanking.dto.TransactionDto;
import com.hcl.retailbanking.entity.Transaction;

/**
 * The interface TransactionService.
 * 
 * @author Vasavi
 *
 */
public interface TransactionService {
	/**
	 * 
	 * @description this method is used to do fund transfer in the application.
	 * @param fundTransferRequestDTO the fundTransferRequestDTO which contains
	 *                               fromAccount,toAccount,amount,transactionType
	 *                               and benefactorName.
	 * @return fundTransferResponseDto
	 */
	public FundTransferResponseDto fundTransfer(FundTransferRequestDto fundTransferRequestDTO);

	public AccountSummaryDto accountSummary(Integer userId);

	/**
	 * From TransactionDto input is taken as month and year from user and
	 * transactions are fetched
	 * 
	 * @param transactionDto
	 * @return list of transactions that are taken place in a month
	 * @throws ParseException
	 */
	public List<Transaction> viewTransactions(TransactionDto transactionDto);

}
