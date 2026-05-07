package com.tfg_rm.backend_restaurantmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * Main class of the Spring Boot application for the Restaurant Manager backend.
 */
@Slf4j
@SpringBootApplication
public class BackendRestaurantManagerApplication {

	/**
	 * Main method to run the Spring Boot application.
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendRestaurantManagerApplication.class, args);

	}    
}
