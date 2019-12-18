package com.hcl.retailbanking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	@Query("select a from Account a where a.userId=:userId")
	public Account findByUserId(@Param("userId") Integer userId);

	public Account findAccountNumberByUserId(Integer userId);

	@Query("select a from Account a where a.userId=:userId and a.accountType=:accountType")
	public Account getAccountByUserIdAndAccountType(@Param("userId") Integer userId,
			@Param("accountType") String accountType);

	@Query("select c from Account c where CONCAT('',c.accountNumber) like %:accountNumber% and accountType =:accountType")
	public List<Account> findByAccountNumber(@Param("accountNumber") String accountNumber, String accountType);

	@Query("select a from Account a where a.accountNumber=:accountNumber")
	public Account getAccountByAccountNumber(@Param("accountNumber") Long accountNumber);

}
