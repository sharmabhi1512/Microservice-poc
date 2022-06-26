package com.abhi.account.controller;

import com.abhi.account.config.AccountsServiceConfig;
import com.abhi.account.model.*;
import com.abhi.account.repository.AccountsRepository;
import com.abhi.account.service.client.CardsFeignClient;
import com.abhi.account.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.annotation.TimedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AccountsController {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountsServiceConfig accountServiceConfig;

    @Autowired
    LoansFeignClient loansFeignClient;

    @Autowired
    CardsFeignClient cardsFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    @PostMapping("/myAccount")
    @Timed(value = "getAccountsDetails.time", description = "Time taken to return the account details")
    public Accounts getAccountsDetails(@RequestBody Customer customer){
        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId());
        if(account != null){
            return account;
        }
        else {
            return null;
        }
    }

    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountServiceConfig.getMsg(), accountServiceConfig.getBuildVersion(),
                accountServiceConfig.getMailDetails(), accountServiceConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }

    @PostMapping("/myCustomerDetails")
    /*@CircuitBreaker(name = "detailsForCustomerSupportApp",fallbackMethod ="myCustomerDetailsFallBack")*/
    /*@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")*/
    public CustomerDetails myCustomerDetails(@RequestHeader("sharmabhi-correlation-id") String correlationid, @RequestBody Customer customer) {
        logger.info("myCustomerDetails() method started");
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(correlationid, customer);
        List<Cards> cards = cardsFeignClient.getCardDetails(correlationid, customer);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        customerDetails.setCards(cards);
        logger.info("myCustomerDetails() method ended");
        return customerDetails;
    }

    private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("sharmabhi-correlation-id") String correlationid, Customer customer, Throwable t) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(correlationid, customer);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        return customerDetails;

    }

    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return "Hello, Welcome to Sharmabhi Kubernetes cluster";
    }

    private String sayHelloFallback(Throwable t) {
        return "Hi, Welcome to Sharmabhi";
    }
}
