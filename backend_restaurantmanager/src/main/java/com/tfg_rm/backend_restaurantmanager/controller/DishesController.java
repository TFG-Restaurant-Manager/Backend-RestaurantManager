package com.tfg_rm.backend_restaurantmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.tfg_rm.backend_restaurantmanager.dto.DishesRequest;
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

    @GetMapping
    public ResponseEntity<List<DishesResponse>> getAll(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        List<DishesResponse> dishes = dishService.getAllDishes(restaurantId);
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishesResponse> getById(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        DishesResponse dish = dishService.getDishById(id, restaurantId);
        return ResponseEntity.ok(dish);
    }

    @PostMapping
    public ResponseEntity<DishesResponse> create(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody DishesRequest request
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        DishesResponse createdDish = dishService.createDish(request, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishesResponse> update(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody DishesRequest request
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        DishesResponse updatedDish = dishService.updateDish(id, request, restaurantId);
        return ResponseEntity.ok(updatedDish);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        dishService.deleteDish(id, restaurantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("category")
    public ResponseEntity<List<String>> getAllCategories(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        List<String> categories = dishService.getAllCategories(restaurantId);
        return ResponseEntity.ok(categories);
    }
}
