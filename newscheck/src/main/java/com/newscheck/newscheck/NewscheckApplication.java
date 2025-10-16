package com.newscheck.newscheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NewscheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewscheckApplication.class, args);
	}

}
