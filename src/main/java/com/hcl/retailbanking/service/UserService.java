package com.hcl.retailbanking.service;

import java.util.List;

import com.hcl.retailbanking.dto.LoginRequestDto;
import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.dto.UserListResponseDto;
import com.hcl.retailbanking.exception.AgeNotMatchedException;
import com.hcl.retailbanking.exception.MobileNumberExistException;
import com.hcl.retailbanking.exception.PasswordInvalidException;

public interface UserService {

	public RegisterResponseDto saveUser(UserDto userDto)
			throws PasswordInvalidException, AgeNotMatchedException, MobileNumberExistException;


	//LoginResponseDto loginUser(LoginRequestDto loginRequestDto);


	List<UserListResponseDto> getAllUser(Integer userId);

}
