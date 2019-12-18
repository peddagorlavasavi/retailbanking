package com.hcl.retailbanking.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hcl.retailbanking.dto.MortgageResponseDto;
import com.hcl.retailbanking.dto.PayeeResponseDto;
import com.hcl.retailbanking.entity.Account;
import com.hcl.retailbanking.entity.Mortgage;
import com.hcl.retailbanking.service.AccountService;
import com.hcl.retailbanking.util.ApiConstant;

@RestController
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
@RequestMapping("/accounts")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * @author Sujal
	 * @description This API is used to create a mortgage account for a saving
	 *              account by the admin
	 * @param userId   is the admin id
	 * @param mortgage details of customer
	 * @return ResponseEntity<MortgageResponseDto>
	 */
	@PostMapping("/mortgage/{userId}")
	public ResponseEntity<MortgageResponseDto> createMortgageAccount(@PathVariable("userId") Integer userId,
			@RequestBody Mortgage mortgage) {
		MortgageResponseDto mortgageResponseDto = new MortgageResponseDto();
		logger.info("inside createMortgageAccount method ");
		Account account = accountService.createMortgageAccount(userId, mortgage);
		if (account != null) {
			logger.info("inside createMortgageAccount method ");
			mortgageResponseDto.setAccountNumber(account.getAccountNumber());
			mortgageResponseDto.setMessage(ApiConstant.CREATE_SUCCESS);
			return ResponseEntity.ok(mortgageResponseDto);
		} else {
			mortgageResponseDto.setMessage(ApiConstant.CREATE_FAILED);
		}

		return ResponseEntity.ok(mortgageResponseDto);

	}

	/**
	 * @author Sujal
	 * @description This api is used fetch Payees details based on customer id from banking-app service
	 * @param userId is login customer Id 
	 * @return PayeeResponseDto is the list of Favorite Payees and response code
	 */
	@GetMapping("/{userId}/payees")
	public ResponseEntity<PayeeResponseDto> getPayees(@PathVariable("userId") Integer userId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		String url = "http://localhost:8083/banking-app/" + userId + "/payees";

		PayeeResponseDto payeeResponseDto = restTemplate.exchange(url, HttpMethod.GET, entity, PayeeResponseDto.class)
				.getBody();
		if (payeeResponseDto != null && payeeResponseDto.getStatusCode().equals(200)) {
			return ResponseEntity.ok(payeeResponseDto);
		} else {
			payeeResponseDto = new PayeeResponseDto();
			payeeResponseDto.setMessage(ApiConstant.FAILED_MSG);
			payeeResponseDto.setStatusCode(204);
			return ResponseEntity.ok(payeeResponseDto);
		}
	}
	
	@GetMapping("/{accountNumber}")
	public Boolean getAccountNumber(@PathVariable("accountNumber") Long accountNumber) {
		return accountService.getAccountNumber(accountNumber);
	}

}
