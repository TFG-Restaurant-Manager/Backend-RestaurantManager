package com.tfg_rm.backend_restaurantmanager;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tfg_rm.backend_restaurantmanager.service.JwtService;

@SpringBootApplication
public class BackendRestaurantManagerApplication {

	public static void main(String[] args) {

		JwtService servicioPrueba = new JwtService();

		String jwt = servicioPrueba.generateToken(50L, 50L, "COCINA");
		System.out.println("\u001B[34m" + LocalDateTime.now() + "\u001B[0m" + jwt);
		SpringApplication.run(BackendRestaurantManagerApplication.class, args);
	}

}
