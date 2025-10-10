package com.quicksight.quicksight_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QuicksightClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuicksightClientApplication.class, args);
	}


}
