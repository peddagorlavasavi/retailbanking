package com.hcl.retailbanking.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.retailbanking.dto.LoginRequestDto;
import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.SearchResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.dto.UserListResponseDto;
import com.hcl.retailbanking.exception.AgeNotMatchedException;
import com.hcl.retailbanking.exception.MobileNumberExistException;
import com.hcl.retailbanking.exception.PasswordInvalidException;
import com.hcl.retailbanking.service.UserService;

/**
 * UserController is the Controller class
 * 
 * @author Hema This Controller is used to save the user details and to verify
 *         the user
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
public class UserController {

	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * 
	 * @param userDto
	 * @return
	 * @throws PasswordInvalidException
	 * @throws AgeNotMatchedException
	 * @throws MobileNumberExistException
	 */

	@PostMapping("")
	public ResponseEntity<RegisterResponseDto> createAccount(@Valid @RequestBody UserDto userDto)
			throws PasswordInvalidException, AgeNotMatchedException, MobileNumberExistException {

		logger.info("saveUser is used to save the user details");
		RegisterResponseDto registerResponseDto = userService.createAccount(userDto);
		if (registerResponseDto != null) {
			return new ResponseEntity<>(registerResponseDto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	/**
	 * 
	 * @param mobileNumber
	 * @param password
	 * @return
	 */
	@PostMapping("/user/login")
	public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto apiLoginRequestDto) {
		logger.info("loginUser is used to verify the user");
		if (apiLoginRequestDto != null) {
			LoginResponseDto apiLoginInfoDto = userService.loginUser(apiLoginRequestDto.getMobileNumber(),
					apiLoginRequestDto.getPassword());
			if (apiLoginInfoDto != null) {
				return new ResponseEntity<>(apiLoginInfoDto, HttpStatus.OK);
			}
		}
		return  new ResponseEntity<>( HttpStatus.OK);
}
	
	/**
	 * @description -> getting all the customers
	 * @param userId
	 * @return List<UserListResponseDto>
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<List<UserListResponseDto>> getAllUser(@PathVariable("userId") Integer userId){
		logger.info("Listing all the users");
		List<UserListResponseDto> userListResponseDto = userService.getAllUser(userId);
		if (userListResponseDto != null) {
			return new ResponseEntity<>(userListResponseDto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
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
	@GetMapping("/{userId}/search")
	public ResponseEntity<List<SearchResponseDto>> getAccountDetails(@PathVariable("userId") Integer userId,
			@RequestParam Long accountNumber) {
		List<SearchResponseDto> accountList = userService.searchAccount(userId, accountNumber);
		if (accountList != null && !accountList.isEmpty()) {
			logger.info("got the list");
			return new ResponseEntity<>(accountList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

}
