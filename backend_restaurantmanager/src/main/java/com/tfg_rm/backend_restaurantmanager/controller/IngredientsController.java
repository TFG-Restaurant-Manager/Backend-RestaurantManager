package com.tfg_rm.backend_restaurantmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg_rm.backend_restaurantmanager.dto.IngredientsResponse;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.IngredientsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ingredients")
public class IngredientsController {

    /** The ingredients service for managing ingredient-related operations. */
    private final IngredientsService ingredientsService;

    /** The JWT service for token generation and validation. */
    private final JwtService jwtService;

    
    @GetMapping
    public ResponseEntity<List<IngredientsResponse>> getAll(
        @RequestHeader("Authorization") String authHeader
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        List<IngredientsResponse> employees = ingredientsService.getAllIngredients(restaurantId);
        return ResponseEntity.ok(employees);
    }
    
}
