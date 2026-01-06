package com.itc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.itc.demo")
public class DonationmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationmanagementApplication.class, args);
	}

}
