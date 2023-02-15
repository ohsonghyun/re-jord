package com.dev6.rejordbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReJordBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReJordBeApplication.class, args);
	}

}
