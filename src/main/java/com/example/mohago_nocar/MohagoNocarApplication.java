package com.example.mohago_nocar;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class MohagoNocarApplication {

	public static void main(String[] args) {
		SpringApplication.run(MohagoNocarApplication.class, args);
	}

}
