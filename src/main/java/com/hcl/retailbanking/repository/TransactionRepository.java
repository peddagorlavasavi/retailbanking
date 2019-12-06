package com.hcl.retailbanking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.Transaction;

/**
 * @author Vasavi
 *
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	/**
	 * findTop5ByFromAccountOrderByTransactionDateDesc() is used to fetch the last five 
	 * records in descending order by providing the account number
	 * @param accountNumber
	 * @return
	 */
	List<Transaction> findTop5ByFromAccountOrderByTransactionIdDesc(Long accountNumber);

	/**
	 * @author Sri Keerthna From the input we will fetch the Informations
	 * @param fromDate
	 * @param toDate
	 * @return
	 */

	@Query("SELECT t FROM Transaction t WHERE t.fromAccount=:accountNumber and t.transactionDate between :fromDate and :toDate")
	List<Transaction> getTransactionsBetweenDates(LocalDate fromDate, LocalDate toDate, Long accountNumber);

}
