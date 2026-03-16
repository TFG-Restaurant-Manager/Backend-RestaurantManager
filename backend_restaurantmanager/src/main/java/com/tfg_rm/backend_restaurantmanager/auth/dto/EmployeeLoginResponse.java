package com.tfg_rm.backend_restaurantmanager.auth.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for employee login responses,
 * containing the employee's ID and a list of restaurant IDs they are associated with.
 */
@Data
@AllArgsConstructor
public class EmployeeLoginResponse {

    /**
     * The employee's ID.
     */
    private Long employeeId;

    /**
     * A list of restaurant names that the employee is associated with.
     */
    private List<String> restaurantNames;
}
