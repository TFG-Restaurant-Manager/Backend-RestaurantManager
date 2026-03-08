package com.tfg_rm.backend_restaurantmanager.auth.dto;

import lombok.Data;

/**
 * Data Transfer Object for employee login requests, 
 * containing the employee's DNI and password.
 */
@Data
public class EmployeeLoginRequest {

    /**
     * The employee's DNI.
     */
    private String dni;

    /**
     * The employee's password.
     */
    private String password;
}
