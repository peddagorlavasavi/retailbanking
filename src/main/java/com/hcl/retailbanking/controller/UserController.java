package com.hcl.retailbanking.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.LoginRequestDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.exception.AgeNotMatchedException;
import com.hcl.retailbanking.exception.MobileNumberExistException;
import com.hcl.retailbanking.exception.PasswordInvalidException;
import com.hcl.retailbanking.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * UserController is the Controller class
 * 
 * @author Hema This Controller is used to save the user details and to verify
 *         the user
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * 
	 * @param userDto
	 * @return
	 * @throws PasswordInvalidException
	 * @throws AgeNotMatchedException
	 * @throws MobileNumberExistException
	 */
	
	@PostMapping("")
	public ResponseEntity<RegisterResponseDto> saveUser(@Valid @RequestBody UserDto userDto)
			throws PasswordInvalidException, AgeNotMatchedException, MobileNumberExistException {
		log.info("saveUser is used to save the user details");
		RegisterResponseDto registerResponseDto = userService.saveUser(userDto);
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
		log.info("loginUser is used to verify the user");
		if(apiLoginRequestDto!=null) {
			LoginResponseDto apiLoginInfoDto = userService.loginUser(apiLoginRequestDto.getMobileNumber(), apiLoginRequestDto.getPassword());
			if (apiLoginInfoDto!=null) {
				return new ResponseEntity<>(apiLoginInfoDto, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
