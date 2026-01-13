package com.itc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(scanBasePackages = "com.itc.demo")
@EnableMethodSecurity
public class DonationmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationmanagementApplication.class, args);
	}

}
