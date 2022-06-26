/**
 * 
 */
package com.abhi.loans.controller;

import java.util.List;

import com.abhi.loans.config.LoansServiceConfig;
import com.abhi.loans.model.Customer;
import com.abhi.loans.model.Loans;
import com.abhi.loans.model.Properties;
import com.abhi.loans.repository.LoansRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Eazy Bytes
 *
 */

@RestController
public class LoansController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoansController.class);

	@Autowired
	private LoansRepository loansRepository;

	@Autowired
	LoansServiceConfig loansConfig;

	@PostMapping("/myLoans")
	public List<Loans> getLoansDetails(@RequestHeader("sharmabhi-correlation-id") String correlationid, @RequestBody Customer customer) {
		logger.info("getLoansDetails() Loan method started");
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDateDesc(customer.getCustomerId());
		logger.info("getLoansDetails() Loan method ended");
		if (loans != null) {
			return loans;
		} else {
			return null;
		}
	}

	@GetMapping("/loans/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		logger.info("getPropertyDetails() Loan method started");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
				loansConfig.getMailDetails(), loansConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		logger.info("getPropertyDetails() Loan method ended");
		return jsonStr;
	}

}
