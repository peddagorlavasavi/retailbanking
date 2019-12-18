package com.hcl.retailbanking.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FavoritePayeeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Integer payeeId;
	String favoriteName;
	Integer customerId;
	Long accountNumber;
	String ifscCode;
	String branchName;
	String bankName;
}
