package com.hcl.retailbanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.retailbanking.entity.Mortgage;

@Repository
public interface MortgageRepository extends JpaRepository<Mortgage, Integer> {

}
