package com.hcl.retailbanking.repository;

import java.util.List;

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
	public Account findByUserId(Integer userId);

	/**
	 * @author Sri Keerthna By using userId find the account number
	 * @param userId
	 * @return
	 */
	public Account findAccountNumberByUserId(Integer userId);

	@Query("select a from Account a where a.userId=:userId and a.accountType=:accountType")
	public Account getAccountByUserIdAndAccountType(@Param("userId") Integer userId,@Param("accountType") String accountType);


	@Query("select c from Account c where CONCAT('',c.accountNumber) like %:accountNumber% and accountType =:accountType")
	public List<Account> findByAccountNumber(@Param("accountNumber")  String accountNumber,String accountType);

}
