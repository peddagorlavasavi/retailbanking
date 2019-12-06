package com.hcl.retailbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.Account;

/**
 * @author Vasavi
 *
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	public Account findByUserId(Integer userId);

	/**
	 * @author Sri Keerthna By using userId find the account number
	 * @param userId
	 * @return
	 */
	public Account findAccountNumberByUserId(Integer userId);


}
