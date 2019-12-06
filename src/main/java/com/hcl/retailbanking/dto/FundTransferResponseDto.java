package com.hcl.retailbanking.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Vasavi
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private Long fromAccount;
	private Long toAccount;
	private Double amount;

}
