package com.hcl.retailbanking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCode {

	private int code;
	private String message;

	public static  final MessageCode RECORD_NOT_FOUND = new MessageCode(101, "Record not found");
	public static  final MessageCode ACCOUNT_INVALID = new MessageCode(102, "Inavalid account");
	public static final MessageCode AMMOUNT_INVALID = new MessageCode(103,
			"Please enter valid amount for transfer");
	public static final  MessageCode SAME_ACCOUNT_INVALID = new MessageCode(104, "You can not transfer to the same account");

	public static final  MessageCode insufficentFunds(Double amount) {
		return new MessageCode(105,
				"Insufficient funds  for transfer.Available balance for transfer "
						+ (amount - 1000));
	}

}