package com.hcl.retailbanking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.util.StringConstant;

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
	 * @return Account
	 */
	public Account findAccountNumberByUserId(Integer userId);

	/**
	 * @author Sujal
	 * @description This method is used to fetch account based on userId and account
	 *              type [savings or mortgage]
	 * @param userId
	 * @param accountType
	 * @return Account
	 */
	@Query("select a from Account a where a.userId=:userId and a.accountType=:accountType")
	public Account getAccountByUserIdAndAccountType(@Param("userId") Integer userId,
			@Param("accountType") String accountType);

	/**
	 * @author Sujal
	 * @description This method is used to fetch the list of account based on
	 *              account number and account type [savings or mortgage]
	 * @param accountNumber
	 * @param accountType
	 * @return List<Account> is list of account
	 */
	@Query("select c from Account c where c.userId not in (select a.userId from Account a where a.accountType='"+StringConstant.MORTGAGE_ACCOUNT_TYPE+"') and CONCAT('',c.accountNumber) like %:accountNumber% and accountType =:accountType")
	public List<Account> findByAccountNumber(@Param("accountNumber") String accountNumber, @Param("accountType") String accountType);

}
