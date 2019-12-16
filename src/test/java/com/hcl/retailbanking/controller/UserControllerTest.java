
package com.hcl.retailbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.hcl.retailbanking.dto.LoginRequestDto;
import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.dto.UserListResponseDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.service.UserService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest {

	@InjectMocks
	UserController userController;

	@Mock
	UserService userService;

	User user = new User();
	UserDto userDto = new UserDto();
	RegisterResponseDto registerResponseDto = new RegisterResponseDto();
	Account account = new Account();
	LoginRequestDto apiLoginRequestDto = new LoginRequestDto();
	LoginResponseDto apiLoginInfoDto = new LoginResponseDto();
	
	
	List<UserListResponseDto> userListResponseDtoList=new ArrayList<>();
	UserListResponseDto userListResponseDto=new UserListResponseDto();
	Mortgage mortgage=new Mortgage();
	
	@Before
	public void setUp() {
		userListResponseDto.setUserId(2);
		mortgage.setEmi(12.0);
		userListResponseDto.setMortgage(mortgage);
		userListResponseDtoList.add(userListResponseDto);
	}
	
	@Test
	public void testGetAllUserPositive() {
		Mockito.when(userService.getAllUser(1)).thenReturn(userListResponseDtoList);
		Integer result = userController.getAllUser(1).getStatusCodeValue();
		assertEquals(200, result);
	}

	
	@Test
	public void testGetAllUserNegative() {
		List<UserListResponseDto> userListResponseDtoLists=null;
		Mockito.when(userService.getAllUser(1)).thenReturn(userListResponseDtoLists);
		Integer result = userController.getAllUser(1).getStatusCodeValue();
		assertEquals(204, result);
	}

	
	
	

}
