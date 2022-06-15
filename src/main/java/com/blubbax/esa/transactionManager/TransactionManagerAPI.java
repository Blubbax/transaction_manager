package com.blubbax.esa.transactionManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class TransactionManagerAPI {

	public static void main(String[] args) {
		SpringApplication.run(TransactionManagerAPI.class, args);
	}

}
