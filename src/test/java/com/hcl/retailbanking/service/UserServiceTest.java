package com.hcl.retailbanking.service;


import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.hcl.retailbanking.dto.LoginResponseDto;
import com.hcl.retailbanking.dto.RegisterResponseDto;
import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.repository.UserRepository;
import com.hcl.retailbanking.util.ApiConstant;
import com.hcl.retailbanking.util.StringConstant;


@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	UserRepository userRepository;

	@Mock
	AccountService accountService;

	UserDto userDto = new UserDto();
	User user = new User();
	RegisterResponseDto registerResponseDto = new RegisterResponseDto();
	LoginResponseDto apiLoginInfoDto = new LoginResponseDto();

	Account account = new Account();

	/*
	 * @Test public void testSaveUserPositive() throws PasswordInvalidException,
	 * AgeNotMatchedException, MobileNumberExistException {
	 * userDto.setFirstName("Hema"); userDto.setLastName("Jayakumar");
	 * userDto.setDob(LocalDate.of(1998, 8, 25));
	 * userDto.setMobileNumber("9894803626"); userDto.setEmail("hema@gmail.com");
	 * userDto.setTypeOfId("Aadhar"); userDto.setIdProofNumber("IND467");
	 * userDto.setAge(21); userDto.setAddress("Chennai");
	 * userDto.setPassword("hemanive"); userDto.setGender("Female");
	 * 
	 * account.setUserId(1223); account.setAccountNumber(100100L);
	 * account.setAccountType(StringConstant.ACCOUNT_TYPE);
	 * account.setIfscCode(StringConstant.IFSC_CODE); account.setBalance(10000.0);
	 * 
	 * registerResponseDto.setMessage(StringConstant.REGISTRATION_STATUS);
	 * registerResponseDto.setAccountNumber(account.getAccountNumber());
	 * registerResponseDto.setBalance(10000.0);
	 * 
	 * user.setUserId(1223);
	 * 
	 * when(userRepository.save(user)).thenReturn(user);
	 * when(accountService.generateAccount(user.getUserId())).thenReturn(account);
	 * 
	 * RegisterResponseDto registerResponseDto =userServiceImpl.saveUser(userDto);
	 * Assert.assertNotNull(registerResponseDto);
	 * 
	 * }
	 */
	
	@Test
	public void testLoginUserPositive() {

		user.setUserId(1);
		user.setFirstName("Hema");
		user.setLastName("Jayakumar");
		user.setDob(LocalDate.of(1998, 8, 25));
		user.setMobileNumber("9894803625");
		user.setEmail("hema@gmail.com");
		user.setTypeOfId("Aadhar");
		user.setIdProofNumber("IND467");
		user.setAge(21);
		user.setAddress("Chennai");
		user.setPassword("hemanive");
		user.setGender("Female");
		user.setStatus(StringConstant.USER_STATUS);

		account.setAccountNumber(100100L);
		account.setAccountType("SAVINGS");
		account.setBalance(1000.0);
		account.setIfscCode("IN35");
		account.setUserId(1);

		apiLoginInfoDto.setUserId(1);
		apiLoginInfoDto.setAccountNumber(100100L);
		apiLoginInfoDto.setUserName("Hema");
		apiLoginInfoDto.setStatus(ApiConstant.SUCCESS);

		Mockito.when(userRepository.getUserByMobileNumber("9894803625")).thenReturn(user);
		LoginResponseDto apiLoginInfoDtos = userServiceImpl.loginUser("9894803625", "hemanive");
		Assert.assertNotNull(apiLoginInfoDtos);
	}

	@Test
	public void testLoginUserNegative() {

		user.setUserId(1);
		user.setFirstName("Hema");
		user.setLastName("Jayakumar");
		user.setDob(LocalDate.of(1998, 8, 25));
		user.setMobileNumber("9894803625");
		user.setEmail("hema@gmail.com");
		user.setTypeOfId("Aadhar");
		user.setIdProofNumber("IND467");
		user.setAge(21);
		user.setAddress("Chennai");
		user.setPassword("hemanive");
		user.setGender("Female");
		user.setStatus(StringConstant.USER_STATUS);

		account.setAccountNumber(100100L);
		account.setAccountType("SAVINGS");
		account.setBalance(1000.0);
		account.setIfscCode("IN35");
		account.setUserId(1);

		apiLoginInfoDto.setUserId(1);
		apiLoginInfoDto.setAccountNumber(100100L);
		apiLoginInfoDto.setUserName("Hema");
		apiLoginInfoDto.setStatus(ApiConstant.SUCCESS);

		Mockito.when(userRepository.getUserByMobileNumber("9894803625")).thenReturn(user);
		LoginResponseDto apiLoginInfoDtos = userServiceImpl.loginUser("9894803625", "hemanive");
		Assert.assertNotNull(apiLoginInfoDtos);
	}

}
