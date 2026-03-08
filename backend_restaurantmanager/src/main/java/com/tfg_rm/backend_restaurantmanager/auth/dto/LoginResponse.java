package com.tfg_rm.backend_restaurantmanager.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for login responses, 
 * containing the authentication token for the logged-in user.
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    /**
     * The authentication token for the logged-in user.
     */
    private String token;
}
