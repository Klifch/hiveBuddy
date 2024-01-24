package com.hivebuddyteam.hivebuddyapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HiveBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiveBuddyApplication.class, args);
	}

}
