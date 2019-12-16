package com.hcl.retailbanking.exception;

public class NotEligibleForMortgageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Customer is not eligible for mortgage.";
	}

}
