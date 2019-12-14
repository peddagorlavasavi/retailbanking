package com.hcl.retailbanking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * findUserByUserId()
	 * 
	 * @description this method will return an user object based on userid
	 * 
	 * @param userId
	 * @return
	 */
	User findUserByUserId(Integer userId);

	/**
	 * getUserByMobileNumber()
	 * 
	 * @description the method getUserByMobileNumber() will return an user based on
	 *              the mobile number provided.
	 * @param mobileNumber
	 * @return
	 */
	@Query("select u from User u where u.mobileNumber=:mobileNumber")
	User getUserByMobileNumber(@Param("mobileNumber") String mobileNumber);

	User findByMobileNumberAndPassword(String mobileNumber, String password);

	//Optional<User> findByUserIdAndRole(Integer userId, String role);

	@Query("select u from User u where u.userId =:userId and u.role =:role")
	User getAdmin(@Param("userId") Integer userId, @Param("role") String role);

	@Query("select u from User u where u.role =:role")
	List<User> getByRole(@Param("role") String role);

	//List<User> findByRole(String role);

}
