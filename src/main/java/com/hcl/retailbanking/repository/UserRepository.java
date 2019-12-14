package com.hcl.retailbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * @description this method will return an user object based on userid
	 * 
	 * @param userId
	 * @return
	 */
	User findUserByUserId(Integer userId);
	
	/**
	 * @description the method getUserByMobileNumber() will return an user 
	 * based on the mobile number provided.
	 * @param mobileNumber
	 * @return
	 */
	@Query("select u from User u where u.mobileNumber=:mobileNumber")
	User getUserByMobileNumber(@Param("mobileNumber") String mobileNumber);
	
	@Query("select c from User c where c.userId=:userId and c.role=:role")
	User findUserByRole(@Param("userId") Integer userId, @Param("role") String role);

	User findByuserId(Integer userId);
}
