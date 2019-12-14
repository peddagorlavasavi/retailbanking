package com.hcl.retailbanking.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.SearchResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.exception.AgeNotMatchedException;
import com.hcl.retailbanking.exception.MobileNumberExistException;
import com.hcl.retailbanking.exception.PasswordInvalidException;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.MortgageRepository;
import com.hcl.retailbanking.repository.UserRepository;
import com.hcl.retailbanking.util.AccountComposer;
import com.hcl.retailbanking.util.ApiConstant;
import com.hcl.retailbanking.util.StringConstant;
import com.hcl.retailbanking.util.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * UserServiceImpl is the service class which implements the method declared in
 * UserService
 * 
 * @author Hema This interface is used to save the user details
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	MortgageRepository mortgageRepository;
	
	@Autowired
	AccountService accountService;

	@Qualifier(value = "accountComposer")
	@Autowired
	AccountComposer<UserDto, User> accountComposer;

	/**
	 * @description This is the method used to save the user details
	 * 
	 * @throws PasswordInvalidException
	 * @throws AgeNotMatchedException
	 * @throws MobileNumberExistException
	 */

	@Override
	@Transactional
	public RegisterResponseDto createAccount(UserDto userDto)
			throws PasswordInvalidException, AgeNotMatchedException, MobileNumberExistException {
		// log.info("saveUser is used to store user details");
		Account account = new Account();
		RegisterResponseDto registerResponseDto = new RegisterResponseDto();

		User userDetails = userRepository.getUserByMobileNumber(userDto.getMobileNumber());

		if (userDetails == null) {
			if (Utils.calculateAge(userDto.getDob()) >= StringConstant.MIN_AGE) {

				if (userDto.getPassword().length() >= StringConstant.PASSWORD_LENGTH) {
					User user = accountComposer.compose(userDto);
					userRepository.save(user);
					account = accountService.generateAccount(user.getUserId(), StringConstant.SAVINGS_ACCOUNT_TYPE);
					registerResponseDto.setMessage(StringConstant.REGISTRATION_STATUS);
					registerResponseDto.setAccountNumber(account.getAccountNumber());
					registerResponseDto.setBalance(account.getBalance());
					return registerResponseDto;
				} else {
					throw new PasswordInvalidException(StringConstant.PASSWORD_VALIDATION_FAILED);
				}
			} else {
				throw new AgeNotMatchedException(StringConstant.AGE_VALIDATION_FAILED);
			}
		} else {
			throw new MobileNumberExistException(StringConstant.MOBILE_VALIDATION_FAILED);
		}
	}

	/**
	 * @description loginUser is the method used to login into user account
	 * @param mobileNumber
	 * @param password
	 * @return
	 */
	@Override
	public LoginResponseDto loginUser(String mobileNumber, String password) {
		log.info("loginUser is used to verify the user");
		User user = userRepository.getUserByMobileNumber(mobileNumber);
		LoginResponseDto apiLoginInfoDto = new LoginResponseDto();
		if (user != null && user.getPassword().equals(password)) {
			Account account = accountService.getAccountDetails(user.getUserId());
			if (account != null) {
				apiLoginInfoDto.setUserId(account.getUserId());
				apiLoginInfoDto.setAccountNumber(account.getAccountNumber());
				apiLoginInfoDto.setUserName(user.getFirstName());
				apiLoginInfoDto.setRole(user.getRole());
				apiLoginInfoDto.setStatus(ApiConstant.LOGIN_SUCCESS);
			} else {
				apiLoginInfoDto.setStatus(ApiConstant.LOGIN_FAILED);
			}
		} else {
			apiLoginInfoDto.setStatus(ApiConstant.FAILED);
		}
		return apiLoginInfoDto;
	}

	/**
	 * @author Sri Keerthna. @since 2019-12-14.
	 * @description using userId and partial account number as input it will fetch
	 *              the user details and mortgage details for that particular
	 *              account.
	 * @param userId
	 * @param accountNumber
	 * @return mortgage details and user details are fetched
	 */
	@Override
	public List<SearchResponseDto> searchAccount(Integer userId, Long accountNumber) {
		User user = userRepository.findUserByRole(userId, StringConstant.ADMIN_ROLE);
		List<SearchResponseDto> list = new ArrayList<>();
		if (user != null) {
			List<Account> availableList = accountRepository.findByAccountNumber("" + accountNumber,
					StringConstant.SAVINGS_ACCOUNT_TYPE);
			log.info("got the account lists");
			availableList.forEach(account -> {
				SearchResponseDto searchResponseDto = new SearchResponseDto();
				User users = userRepository.findUserByUserId(account.getUserId());
				BeanUtils.copyProperties(users, searchResponseDto);
				Mortgage mortgage = mortgageRepository.findByAccountNumber(account.getAccountNumber());
				if (mortgage != null)
					searchResponseDto.setMortgage(mortgage);
				list.add(searchResponseDto);
			});
		}
		return list;
	}

}
