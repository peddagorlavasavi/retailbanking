
package com.hcl.retailbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.LoginRequestDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.SearchResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.exception.AgeNotMatchedException;
import com.hcl.retailbanking.exception.MobileNumberExistException;
import com.hcl.retailbanking.exception.PasswordInvalidException;
import com.hcl.retailbanking.service.UserService;
import com.hcl.retailbanking.util.ApiConstant;
import com.hcl.retailbanking.util.StringConstant;
import com.hcl.retailbanking.entity.Mortgage;

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
	List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();

	@Test
	public void testLoginUserPositive() {
		user.setMobileNumber("9894803625");
		user.setPassword("hemanive");
		user.setFirstName("Hema");

		apiLoginRequestDto.setMobileNumber("9894803625");
		apiLoginRequestDto.setPassword("hemanive");

		account.setUserId(1);
		account.setAccountNumber(100100L);
		account.setBalance(10000.0);
		account.setAccountType(StringConstant.SAVINGS_ACCOUNT_TYPE);
		account.setIfscCode(StringConstant.IFSC_CODE);

		apiLoginInfoDto.setUserId(1);
		apiLoginInfoDto.setAccountNumber(100100L);
		apiLoginInfoDto.setUserName("Hema");
		apiLoginInfoDto.setStatus(ApiConstant.SUCCESS);

		Mockito.when(userService.loginUser("9894803625", "hemanive")).thenReturn(apiLoginInfoDto);
		Integer result = userController.loginUser(apiLoginRequestDto).getStatusCodeValue();
		assertEquals(200, result);
	}

	@Test
	public void testLoginUserNegative() {
		Mockito.when(userService.loginUser("9894803620", "hemanive")).thenReturn(apiLoginInfoDto);
		Integer result = userController.loginUser(apiLoginRequestDto).getStatusCodeValue();
		assertEquals(200, result);
	}

	@Test
	public void testSaveUserPositive()
			throws PasswordInvalidException, AgeNotMatchedException, MobileNumberExistException {
		userDto.setFirstName("Hema");
		userDto.setLastName("Jayakumar");
		userDto.setDob(LocalDate.of(1998, 8, 25));
		userDto.setMobileNumber("9894803625");
		userDto.setEmail("hema@gmail.com");
		userDto.setTypeOfId("Aadhar");
		userDto.setIdProofNumber("IND467");
		userDto.setAge(21);
		userDto.setAddress("Chennai");
		userDto.setPassword("hemanive");
		userDto.setGender("Female");

		account.setUserId(1);
		account.setAccountNumber(100100L);
		account.setBalance(10000.0);

		registerResponseDto.setMessage(StringConstant.REGISTRATION_STATUS);
		registerResponseDto.setAccountNumber(100100L);
		registerResponseDto.setBalance(10000.0);

		Mockito.when(userService.createAccount(userDto)).thenReturn(registerResponseDto);
		Integer result = userController.createAccount(userDto).getStatusCodeValue();
		assertEquals(200, result);
	}

	@Test
	public void testSaveUserNegative()
			throws PasswordInvalidException, AgeNotMatchedException, MobileNumberExistException {
		Mockito.when(userService.createAccount(userDto)).thenReturn(null);
		Integer result = userController.createAccount(userDto).getStatusCodeValue();
		assertEquals(406, result);
	}

	List<SearchResponseDto> userListResponseDtoList = new ArrayList<>();
	SearchResponseDto searchResponseDto = new SearchResponseDto();
	Mortgage mortgage = new Mortgage();

	@Before
	public void setUp() {
		searchResponseDto.setUserId(2);
		mortgage.setEmi(12.0);
		searchResponseDto.setMortgage(mortgage);
		userListResponseDtoList.add(searchResponseDto);
	}

	@Test
	public void testGetAllUserPositive() {
		Mockito.when(userService.getAllUser(1)).thenReturn(userListResponseDtoList);
		Integer result = userController.getAllUser(1).getStatusCodeValue();
		assertEquals(200, result);
	}

	@Test
	public void testGetAllUserNegative() {
		List<SearchResponseDto> userListResponseDtoLists = null;
		Mockito.when(userService.getAllUser(1)).thenReturn(userListResponseDtoLists);
		Integer result = userController.getAllUser(1).getStatusCodeValue();
		assertEquals(204, result);
	}

	@Test
	public void searchAccountPositiveTest() {
		user.setUserId(1);
		account.setAccountNumber(12345678L);
		SearchResponseDto searchResponseDto = new SearchResponseDto();
		searchResponseDto.setUserId(1);
		searchResponseDto.setMortgage(mortgage);
		searchResponseDtoList.add(searchResponseDto);
		Mockito.when(userService.searchAccount(1, 12345678L)).thenReturn(searchResponseDtoList);
		Integer result = userController.getAccountDetails(1, 12345678L).getStatusCodeValue();
		assertEquals(200, result);
	}

	@Test
	public void searchAccountNegativeTest() {
		user.setUserId(1);
		account.setAccountNumber(12345678L);
		Mockito.when(userService.searchAccount(1, 12345678L)).thenReturn(searchResponseDtoList);
		Integer result = userController.getAccountDetails(1, 12345678L).getStatusCodeValue();
		assertEquals(204, result);
	}

	@Test
	public void searchAccountNegativeNullTest() {
		user.setUserId(1);
		account.setAccountNumber(12345678L);
		List<SearchResponseDto> nullResponseDtoList = null;
		Mockito.when(userService.searchAccount(1, 12345678L)).thenReturn(nullResponseDtoList);
		Integer result = userController.getAccountDetails(1, 12345678L).getStatusCodeValue();
		assertEquals(204, result);
	}

}
