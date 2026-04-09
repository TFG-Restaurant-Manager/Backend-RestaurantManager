package com.tfg_rm.backend_restaurantmanager.controller;
import com.tfg_rm.backend_restaurantmanager.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.dto.EmployeeWithSchedulesResponse;
import com.tfg_rm.backend_restaurantmanager.dto.SchedulesRequest;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Java class used to manage the employees of the restaurant.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    /** The employee service for managing employee-related operations. */
    private final EmployeeService employeeService;

    /** The JWT service for token generation and validation. */
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<EmployeeEntity> create(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody EmployeeRegisterRequest request
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        EmployeeEntity employee = employeeService.registerEmployee(request, restaurantId);
        return ResponseEntity.ok(employee);
    }

    /**
     * Endpoint to get the employee's information along with their schedules.
     * @param authHeader The Authorization header containing the JWT token.
     * @return The response containing the employee's information and schedules.
     */
    @GetMapping("/me")
    public ResponseEntity<EmployeeWithSchedulesResponse> getCurrentEmployee(
        @RequestHeader("Authorization") String authHeader
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);
        Long employeeId = jwtService.getUserId(token);

        EmployeeWithSchedulesResponse info = employeeService.getEmployeeInfo(restaurantId, employeeId);
        return ResponseEntity.ok(info);
    }

    // Este pa los horarios
    @PutMapping("{id}/schedules")
    public ResponseEntity<Boolean> updateSchedules(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody List<SchedulesRequest> request
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        Boolean isUpdated = employeeService.updateSchedules(request, id, restaurantId);
        return ResponseEntity.ok(isUpdated);
    }
    
    @GetMapping
    public ResponseEntity<List<EmployeeWithSchedulesResponse>> getAll(
        @RequestHeader("Authorization") String authHeader
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        List<EmployeeWithSchedulesResponse> employees = employeeService.getAllEmployees(restaurantId);
        return ResponseEntity.ok(employees);
    }

    @PutMapping("{id}")
    public ResponseEntity<Boolean> update(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody EmployeeRegisterRequest request
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        Boolean isUpdated = employeeService.updateEmployee(request, id, restaurantId);
        return ResponseEntity.ok(isUpdated);
    }

    @PutMapping("{id}/password")
    public ResponseEntity<Boolean> updatePassword(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody String request
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        Boolean isUpdated = employeeService.updatePassword(request, id, restaurantId);
        return ResponseEntity.ok(isUpdated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        Boolean isDelete = employeeService.deleteEmployee(id, restaurantId);

        return ResponseEntity.ok(isDelete);
    }
}
