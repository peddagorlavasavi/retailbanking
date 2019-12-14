package com.hcl.retailbanking.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.retailbanking.dto.LoginRequestDto;
import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.dto.UserListResponseDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.exception.AgeNotMatchedException;
import com.hcl.retailbanking.exception.MobileNumberExistException;
import com.hcl.retailbanking.exception.PasswordInvalidException;
import com.hcl.retailbanking.repository.AccountRepository;
import com.hcl.retailbanking.repository.MortgageRepository;
import com.hcl.retailbanking.repository.UserRepository;
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

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	MortgageRepository mortgagerepository;

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
		// log.info("saveUser is used to store user details");
		Account account = new Account();
		RegisterResponseDto registerResponseDto = new RegisterResponseDto();
		User userDetails = userRepository.getUserByMobileNumber(userDto.getMobileNumber());
		if (userDetails == null) {
			if (Utils.calculateAge(userDto.getDob()) >= StringConstant.MIN_AGE) {
				if (userDto.getPassword().length() >= StringConstant.PASSWORD_LENGTH) {
					User user = setUserData(userDto);
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
	 * 
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
	 * 
	 * @description loginUser is the method used to login into user account
	 * @param mobileNumber
	 * @param password
	 * @return
	 */
	/*
	 * @Override public LoginResponseDto loginUser(LoginRequestDto loginRequestDto)
	 * { logger.info("loginUser is used to verify the user"); User user =
	 * userRepository.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(
	 * ), loginRequestDto.getPassword()); LoginResponseDto loginResponseDto = new
	 * LoginResponseDto(); if (user.getRole().equalsIgnoreCase(StringConstant.ROLE))
	 * { BeanUtils.copyProperties(user, loginResponseDto); return loginResponseDto;
	 * } else { BeanUtils.copyProperties(user, loginResponseDto); return
	 * loginResponseDto; } }
	 */
	
	/**
	 * @description -> getAllUser is used to get all users
	 * @param userId
	 */
	@Override
	public List<UserListResponseDto> getAllUser(Integer userId) {
		User user = userRepository.getAdmin(userId, StringConstant.ROLE);
		logger.info("inside getAllUser " + user.getUserId());
		List<UserListResponseDto> userListResponseDtoList = new ArrayList<>();
		if (user != null) {
			List<User> userList = userRepository.getByRole(StringConstant.CUSTOMER);

			userList.forEach(users -> {
				logger.info("inside getAllUser " + users.getUserId());

				UserListResponseDto userListResponseDto = new UserListResponseDto();
				Account account = accountRepository.findByUserId(users.getUserId());

				if (account != null) {
					logger.info("inside getAllUser "+account.getAccountNumber());

					BeanUtils.copyProperties(users, userListResponseDto);
					BeanUtils.copyProperties(account, userListResponseDto);

					Mortgage mortgage = mortgagerepository.findByAccountNumber(account.getAccountNumber());
					if (mortgage != null) {
						//BeanUtils.copyProperties(mortgage, userListResponseDto);
						userListResponseDto.setMortgage(mortgage);
					}
				}
				userListResponseDtoList.add(userListResponseDto);
			});
		}
		return userListResponseDtoList;
	}

}
