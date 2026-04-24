package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.IngredientRequest;
import com.tfg_rm.backend_restaurantmanager.dto.IngredientsResponse;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.IngredientsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Long restaurantId = getRestaurantId(authHeader);
        List<IngredientsResponse> ingredients = ingredientsService.getAllIngredients(restaurantId);
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping
    public ResponseEntity<IngredientsResponse> create(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody IngredientRequest request
    ) {
        Long restaurantId = getRestaurantId(authHeader);
        IngredientsResponse response = ingredientsService.createIngredient(restaurantId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientsResponse> update(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody IngredientRequest request
    ) {
        Long restaurantId = getRestaurantId(authHeader);
        IngredientsResponse response = ingredientsService.updateIngredient(restaurantId, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id
    ) {
        Long restaurantId = getRestaurantId(authHeader);
        ingredientsService.deleteIngredient(restaurantId, id);
        return ResponseEntity.noContent().build();
    }

    private Long getRestaurantId(String authHeader) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");
        /* Validate the token and extract user details */
        return jwtService.getRestaurantId(token);
    }
}
