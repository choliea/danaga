package com.danaga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AvengersApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvengersApplication.class, args);
	}

}
