package com.hcl.retailbanking.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.SearchResponseDto;
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

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
		Account account = new Account();
		RegisterResponseDto registerResponseDto = new RegisterResponseDto();

		User userDetails = userRepository.getUserByMobileNumber(userDto.getMobileNumber());

		if (userDetails == null) {
			if (Utils.calculateAge(userDto.getDob()) >= StringConstant.MIN_AGE) {

				if (userDto.getPassword().length() >= StringConstant.PASS_LENGTH) {
					User user = accountComposer.compose(userDto);
					userRepository.save(user);
					account = accountService.generateAccount(user.getUserId(), StringConstant.SAVINGS_ACCOUNT_TYPE);
					registerResponseDto.setMessage(StringConstant.REGISTRATION_STATUS);
					registerResponseDto.setAccountNumber(account.getAccountNumber());
					registerResponseDto.setBalance(account.getBalance());
					return registerResponseDto;
				} else {
					throw new PasswordInvalidException(StringConstant.PASS_VALIDATION_FAILED);
				}
			} else {
				throw new AgeNotMatchedException(StringConstant.AGE_VALIDATION_FAILED);
			}
		} else {
			throw new MobileNumberExistException(StringConstant.MOBILE_VALIDATION_FAILED);
		}
	}

	/**
	 * @author  Hema
	 * @description -> getAllUser is used to get all users
	 * @param userId
	 * @throws InvalidAdminException
	 */
	@Override
	public List<SearchResponseDto> getAllUser(Integer userId) {
	

	
		User user = userRepository.getAdmin(userId, StringConstant.ROLE);
		logger.info("inside getAllUser");
		List<SearchResponseDto> searchResponseDtos = new ArrayList<>();
		if (user != null) {
			List<User> userList = userRepository.getByRole(StringConstant.CUSTOMER);

			userList.forEach(users -> {
				logger.info("inside getAllUser");

				SearchResponseDto searchResponseDto = new SearchResponseDto();
				Account account = accountRepository.getAccountByUserIdAndAccountType(user.getUserId(),
						StringConstant.SAVINGS_ACCOUNT_TYPE);
				Account account1 = accountRepository.getAccountByUserIdAndAccountType(user.getUserId(),
						StringConstant.MORTGAGE_ACCOUNT_TYPE);

				if (account != null) {
					logger.info("inside getAllUser");
					UserListResponseDto userListResponseDto = new UserListResponseDto();
					
					

					if (account1 != null) {
						Mortgage mortgage = mortgageRepository.findByAccountNumber(account1.getAccountNumber());
						if (mortgage != null) {
							userListResponseDto.setMortgage(mortgage);
						}
						BeanUtils.copyProperties(account1, userListResponseDto);						
						searchResponseDto.setMortgage(mortgage);
					}
				}
				BeanUtils.copyProperties(account, searchResponseDto);
				BeanUtils.copyProperties(users, searchResponseDto);

				searchResponseDtos.add(searchResponseDto);
			});
		}
		return searchResponseDtos;
	}

	/**
	 * @description loginUser method is for customer login
	 * @param mobileNumber
	 * @param password
	 * @return LoginResponseDto
	 */
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
			List<Account> availableList = accountRepository.findByAccountNumber(""+accountNumber,
					StringConstant.SAVINGS_ACCOUNT_TYPE);
			log.info("got the account lists");
			availableList.forEach(account -> {

				User user1 = userRepository.findUserByRole(account.getUserId(), StringConstant.CUSTOMER_ROLE);

				SearchResponseDto searchResponseDto = new SearchResponseDto();
				logger.info("inside searchAccount " );

				if (user1!=null) {
					Account account1 = accountRepository.getAccountByUserIdAndAccountType(user1.getUserId(),
							StringConstant.MORTGAGE_ACCOUNT_TYPE);
					UserListResponseDto userListResponseDto = new UserListResponseDto();

					if (account1 != null) {
						Mortgage mortgage = mortgageRepository.findByAccountNumber(account1.getAccountNumber());
						if (mortgage != null) {
							// BeanUtils.copyProperties(mortgage, userListResponseDto);
							userListResponseDto.setMortgage(mortgage);
						}
						BeanUtils.copyProperties(account1, userListResponseDto);
						searchResponseDto.setMortgage(mortgage);
					}
				}
				logger.info("inside ZZZZZ " + account.getAccountNumber());

				BeanUtils.copyProperties(account, searchResponseDto);
				BeanUtils.copyProperties(user1, searchResponseDto);
				list.add(searchResponseDto);

			});
		}
		return list;
	}

}
