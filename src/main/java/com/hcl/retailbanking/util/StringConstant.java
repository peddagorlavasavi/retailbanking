package com.hcl.retailbanking.util;

public class StringConstant {

    private StringConstant(){ }

	public static final String DEBIT = "DEBIT";
	public static final String CREDIT = "CREDIT";
	
    public static final String ACTIVE_STATUS = "ACTIVE";
	public static final String DEACTIVE_STATUS = "DEACTIVE";
	
	public static final String USER_STATUS = "ACTIVE";
	public static final String ACCOUNT_TYPE = "SAVINGS"
			+ "";
	public static final String IFSC_CODE = "SBI000012";
	
	public static final String REGISTRATION_STATUS = "Registered Successfully";
	
	public static final String FIRSTNAME_MANDATORY = "First Name is mandatory";
	public static final String LASTNAME_MANDATORY = "Last Name is mandatory";
	public static final String MOBILENUMBER_MANDATORY = "Mobile Number is mandatory";
	public static final String MOBILENUMBER_VALID = "Invalid mobileNumber. Please enter valid mobileNumber";
	public static final String EMAIL_VALID = "Invalid email. Please enter valid email";
	public static final String PASSWORD_MANDATORY = "Password is mandatory";
	public static final String ACCOUNT_NOT_FOUND = "The Account is not available";

	public static final Integer PASSWORD_LENGTH = 8;
	public static final Integer MIN_AGE=18;
	
	public static final String PASSWORD_VALIDATION_FAILED = "Please Enter the Password more than 7 characters";
	public static final String AGE_VALIDATION_FAILED ="Age should be greater than 18";
	public static final String MOBILE_VALIDATION_FAILED = "Mobile Number already exists.Require new mobile number for registration.";
}

