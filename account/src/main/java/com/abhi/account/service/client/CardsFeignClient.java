package com.abhi.account.service.client;

import java.util.List;

import com.abhi.account.model.Cards;
import com.abhi.account.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("cards")
public interface CardsFeignClient {

	@RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = "application/json")
	List<Cards> getCardDetails(@RequestHeader("sharmabhi-correlation-id") String correlationid, @RequestBody Customer customer);
}
