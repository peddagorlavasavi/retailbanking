package com.hcl.retailbanking.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.exception.AgeNotMatchedException;
import com.hcl.retailbanking.exception.MobileNumberExistException;
import com.hcl.retailbanking.exception.PasswordInvalidException;
import com.hcl.retailbanking.repository.UserRepository;
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
	AccountService accountService;

	/**
	 * saveUser is the method used to save the user details
	 * 
	 * @throws PasswordInvalidException
	 * @throws AgeNotMatchedException
	 * @throws MobileNumberExistException
	 */

	@Override
	@Transactional
	public RegisterResponseDto saveUser(UserDto userDto)
			throws PasswordInvalidException, AgeNotMatchedException, MobileNumberExistException {
		//log.info("saveUser is used to store user details");
		Account account = new Account();
		RegisterResponseDto registerResponseDto = new RegisterResponseDto();
		User userDetails = userRepository.getUserByMobileNumber(userDto.getMobileNumber());
		if (userDetails == null) {
			if (Utils.calculateAge(userDto.getDob()) >= StringConstant.MIN_AGE) {
				if (userDto.getPassword().length() >= StringConstant.PASSWORD_LENGTH) {
					User user=setUserData(userDto);
					userRepository.save(user);
					account = accountService.generateAccount(user.getUserId());
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
	 * setUserData()
	 * @description this method is used to set user object
	 * @param userDto
	 * @return
	 */
	private User setUserData(UserDto userDto) {
		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setDob(userDto.getDob());
		user.setMobileNumber(userDto.getMobileNumber());
		user.setEmail(userDto.getEmail());
		user.setTypeOfId(userDto.getTypeOfId());
		user.setIdProofNumber(userDto.getIdProofNumber());
		user.setAge(Utils.calculateAge(userDto.getDob()));
		user.setAddress(userDto.getAddress());
		user.setPassword(userDto.getPassword());
		user.setGender(userDto.getGender());
		user.setStatus(StringConstant.USER_STATUS);
		return user;
	}

	/**
	 * loginUser()
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
				apiLoginInfoDto.setStatus(ApiConstant.LOGIN_SUCCESS);
			} else {
				apiLoginInfoDto.setStatus(ApiConstant.LOGIN_FAILED);
			}
		} else {
			apiLoginInfoDto.setStatus(ApiConstant.FAILED);
		}
		return apiLoginInfoDto;
	}

}
