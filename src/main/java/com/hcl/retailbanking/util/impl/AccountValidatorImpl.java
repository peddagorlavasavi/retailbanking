package com.hcl.retailbanking.util.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.UserRepository;
import com.hcl.retailbanking.util.AccountValidator;

/**
 * 
 * @author Sujal
 * @description account validator is used to validate the customer whether the
 *              customer is eligible to take the mortgage amount or not
 *
 */
@Component("accountValidator")
public class AccountValidatorImpl implements AccountValidator<Mortgage> {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public Boolean validate(Mortgage mortgage) {
		User user = userRepository.findUserByUserId(mortgage.getCustomerId());

		if (user != null)
			return validateEMI(user, mortgage);
		return false;
	}

	private Boolean validateEMI(User user, Mortgage mortgage) {
		Double salary = user.getSalary();
		if (((salary / 2) - (salary / 10)) >= mortgage.getEmi())
			return true;
		return false;
	}

}
