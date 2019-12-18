package com.hcl.retailbanking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findUserByUserId(Integer userId);

	@Query("select u from User u where u.mobileNumber=:mobileNumber")
	User getUserByMobileNumber(@Param("mobileNumber") String mobileNumber);

	@Query("select c from User c where c.userId=:userId and c.role=:role")
	User findUserByRole(@Param("userId") Integer userId, @Param("role") String role);

	User findByuserId(Integer userId);

	User findByMobileNumberAndPassword(String mobileNumber, String password);

	@Query("select u from User u where u.userId =:userId and u.role =:role")
	User getAdmin(@Param("userId") Integer userId, @Param("role") String role);

	@Query("select u from User u where u.role =:role")
	List<User> getByRole(@Param("role") String role);

}
