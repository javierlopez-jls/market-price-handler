package com.scib.fx.mph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class})
public class MarketPriceHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketPriceHandlerApplication.class, args);
	}

}
