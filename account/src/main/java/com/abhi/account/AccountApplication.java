package com.abhi.account;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// To scan all our beans present in the packages
@ComponentScans({@ComponentScan("com.abhi.account.controller")})
//To scan all the repository classes present inside the packages
@EnableJpaRepositories("com.abhi.account.repository")
//To scan all the model classes present inside the packages
@EntityScan("com.abhi.account.model")
@RefreshScope
@EnableFeignClients
// To enable the loan balancer at client side.
public class AccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}
}
