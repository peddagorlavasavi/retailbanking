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
	 *              the mobile number provided. based on the mobile number provided.
	 * @param mobileNumber
	 * @return
	 */
	@Query("select u from User u where u.mobileNumber=:mobileNumber")
	User getUserByMobileNumber(@Param("mobileNumber") String mobileNumber);

	/**
	 * @author Hema
	 * @description the method findUserByRole() will return an user based on the
	 *              role and userId
	 * @param userId
	 * @param role
	 * @return User
	 */
	@Query("select c from User c where c.userId=:userId and c.role=:role")
	User findUserByRole(@Param("userId") Integer userId, @Param("role") String role);

	/**
	 * @author Vasavi
	 * @description the method findByuserId() will return an user based on the
	 *              userId
	 * @param userId
	 * @return User
	 */
	User findByuserId(Integer userId);

	/**
	 * @description the method findByMobileNumberAndPassword() will return an user
	 *              based on the mobile number and password
	 * @param mobileNumber
	 * @param password
	 * @return
	 */
	User findByMobileNumberAndPassword(String mobileNumber, String password);

	/**
	 * @author Hema
	 * @description the method findUserByRole() will return an user based on the
	 *              role and userId
	 * @param userId
	 * @param role
	 * @return User
	 */
	@Query("select u from User u where u.userId =:userId and u.role =:role")
	User getAdmin(@Param("userId") Integer userId, @Param("role") String role);

	/**
	 * @description the method findUserByRole() will return the list of user based
	 *              on the role
	 * @param userId
	 * @param role
	 * @return User
	 */
	@Query("select u from User u where u.role =:role")
	List<User> getByRole(@Param("role") String role);

}
