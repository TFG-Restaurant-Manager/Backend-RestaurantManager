package com.tfg_rm.backend_restaurantmanager.dto;

import lombok.Data;

/**
 * Data Transfer Object for client login requests, 
 * containing the client's email, password, and the restaurant ID they wish to access.
 */
@Data
public class ClientLoginRequest {

    /**
     * The client's email address.
     */
    private String email;

    /**
     * The client's password.
     */
    private String password;

    /**
     * The ID of the restaurant the client wants to access.
     */
    private Long restaurantId;
}