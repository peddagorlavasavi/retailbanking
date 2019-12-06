package com.hcl.retailbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * findUserByUserId()
	 * @description this method will return an user object based on userid
	 * 
	 * @param userId
	 * @return
	 */
	User findUserByUserId(Integer userId);
	
	/**
	 * getUserByMobileNumber()
	 * @description the method getUserByMobileNumber() will return an user 
	 * based on the mobile number provided.
	 * @param mobileNumber
	 * @return
	 */
	@Query("select u from User u where u.mobileNumber=:mobileNumber")
	User getUserByMobileNumber(@Param("mobileNumber") String mobileNumber);
	
}
