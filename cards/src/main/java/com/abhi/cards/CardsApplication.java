package com.abhi.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScans({ @ComponentScan("com.abhi.cards.controller") })
// @ComponentScans act as a container annotation that aggregates several @ComponentScan annotations.
@EnableJpaRepositories("com.abhi.cards.repository")
//This annotation to enable JPA repositories. Will scan the package of the annotated configuration class for Spring Data repositories by default.
@EntityScan("com.abhi.cards.model")
//@EntityScan annotation is used when entity classes are not placed in the main application package or its sub-packages
public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
