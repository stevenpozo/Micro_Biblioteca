package com.microservice_loan.microservice_loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class MicroserviceLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceLoanApplication.class, args);
	}

}
