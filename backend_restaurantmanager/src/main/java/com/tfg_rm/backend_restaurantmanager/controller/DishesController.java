package com.tfg_rm.backend_restaurantmanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg_rm.backend_restaurantmanager.dto.DishesResponse;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.DishService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dish")
public class DishesController {
    
    /** The dish service for managing dish-related operations. */
    private final DishService dishService;


    /** The JWT service for token generation and validation. */
    private final JwtService jwtService;

    @GetMapping("/info")
    public ResponseEntity<List<DishesResponse>> getRestaurantDishes(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        List<DishesResponse> dishes = dishService.getDishesInfo(restaurantId);
        return ResponseEntity.ok(dishes);
    }
}
