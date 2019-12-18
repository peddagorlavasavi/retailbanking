package com.hcl.retailbanking.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mortgage")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Mortgage implements Serializable{

	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer mortgageId;
	private String mortgageType;
	private Double interest;
	private Double amount;
	private Integer tenure;
	private Double emi;
	
	@Transient
	private Integer customerId;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "account_number")
	private Account account;

}
