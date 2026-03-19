package com.tfg_rm.backend_restaurantmanager.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for employee token requests, 
 * containing the employee's ID and the restaurant ID they wish to access.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTokenRequest {

    /**
     * The employee's code.
     */
    private String code;

    /**
     * The employee's password.
     */
    private String password;
}
