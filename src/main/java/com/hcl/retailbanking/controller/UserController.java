package com.hcl.retailbanking.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.retailbanking.dto.RegisterResponseDto;
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
	/*
	 * @PostMapping("/user/login") public ResponseEntity<LoginResponseDto>
	 * userLogin(@RequestBody LoginRequestDto loginRequestDto) {
	 * logger.info("Verifying the user"); LoginResponseDto loginResponseDto =
	 * userService.loginUser(loginRequestDto); if (loginResponseDto != null) {
	 * return new ResponseEntity<>(loginResponseDto, HttpStatus.OK); } else { return
	 * new ResponseEntity<>(HttpStatus.NO_CONTENT); } }
	 */
	
	/**
	 * @description -> getting all the customers
	 * @param userId
	 * @return List<UserListResponseDto>
	 */
	@PostMapping("/{userId}")
	public ResponseEntity<List<UserListResponseDto>> getAllUser(@PathVariable("userId") Integer userId){
		logger.info("Listing all the users");
		List<UserListResponseDto> userListResponseDto = userService.getAllUser(userId);
		if (userListResponseDto != null) {
			return new ResponseEntity<>(userListResponseDto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}


}
