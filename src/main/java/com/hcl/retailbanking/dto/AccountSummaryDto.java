package com.hcl.retailbanking.dto;

import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Transaction;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vasavi
 *
 */
public class AccountSummaryDto implements Serializable {

	private static final long serialVersionUID = 1L;

	Account account;
	List<Transaction> transactions;
	String message;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
