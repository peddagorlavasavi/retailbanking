package com.hcl.retailbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.Account;

/**
 * @author Vasavi
 *
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	@Query("select a from Account a where a.userId=:userId")
	public Account findByUserId(@Param("userId") Integer userId);

	/**
	 * @author Sri Keerthna By using userId find the account number
	 * @param userId
	 * @return
	 */
	public Account findAccountNumberByUserId(Integer userId);


}
