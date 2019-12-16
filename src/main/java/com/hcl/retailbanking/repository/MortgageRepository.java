package com.hcl.retailbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.Mortgage;

@Repository
public interface MortgageRepository extends JpaRepository<Mortgage, Integer> {

	/**
	 * @author Sujal
	 * @description This method is used to return an mortgage details based on
	 *              account number
	 * @param accountNumber of customer
	 * @return Mortgage object
	 */
	@Query("select m from Mortgage m where m.account.accountNumber=:accountNumber")
	Mortgage findByAccountNumber(@Param("accountNumber") Long accountNumber);

}
