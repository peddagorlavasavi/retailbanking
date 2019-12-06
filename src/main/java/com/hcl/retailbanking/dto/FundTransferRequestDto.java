package com.hcl.retailbanking.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Vasavi
 *
 */
@Setter
@Getter
@ToString
public class FundTransferRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long fromAccount;
	private Long toAccount;
	private Double amount;
	private String benefactorName;

}
