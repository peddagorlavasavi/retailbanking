package com.hcl.retailbanking.util.Impl;

import org.springframework.stereotype.Component;

import com.hcl.retailbanking.dto.UserDto;
import com.hcl.retailbanking.entity.User;
import com.hcl.retailbanking.util.AccountComposer;
import com.hcl.retailbanking.util.StringConstant;
import com.hcl.retailbanking.util.Utils;

@Component(value ="accountComposer")
public class UserComposer implements AccountComposer<UserDto, User> {

	@Override
	public User compose(UserDto userDto) {
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
		user.setSalary(userDto.getSalary());
		user.setRole(StringConstant.CUSTOMER_ROLE);
		return user;
	}

}
