package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.LoginResponse;
import com.tfg_rm.backend_restaurantmanager.dto.RestaurantRequest;
import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.RestaurantService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Java class used to manage the authentication of the users of the application
 */
@RequiredArgsConstructor // Me hace un constructor con todos los campos final
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    /** The JWT service for token generation and validation. */
    private final JwtService jwtService;

    /** Servicio del restaurante */
    private final RestaurantService restaurantService;

    /**
     * Endpoint for generating a JWT token for an employee. Validates the employee's
     * access to the specified restaurant and generates a token if successful.
     * 
     * @param request The request containing the employee's ID and the restaurant's
     *                ID.
     * @return The response containing the generated JWT token.
     */
    @PostMapping
    public ResponseEntity<LoginResponse> create(@RequestBody RestaurantRequest request) {
        EmployeeEntity manager = restaurantService.createRestaurantAndManager(request);

        String token = jwtService.generateToken(
                manager.getId(),
                manager.getRestaurant().getId(),
                Role.MANAGER);

        return ResponseEntity.ok(new LoginResponse(token, Role.MANAGER));
    }

    /**
     * Endpoint for deleting a restaurant by its ID.
     * 
     * @param id The ID of the restaurant to be deleted.
     * @return A ResponseEntity without content to indicate successful deletion.
     */
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestHeader("Authorization") String authHeader) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
