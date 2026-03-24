package com.tfg_rm.backend_restaurantmanager.controller;

import java.util.List;

import com.tfg_rm.backend_restaurantmanager.dto.DishesResponse;
import com.tfg_rm.backend_restaurantmanager.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.dto.EmployeeWithSchedulesResponse;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @PostMapping("/register")
    public ResponseEntity<EmployeeEntity> employeeRegister(
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
    @GetMapping("/info")
    public ResponseEntity<EmployeeWithSchedulesResponse> getEmployeeInfo(
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

    // @GetMapping("/tables-orders")
    // public ResponseEntity<List<RestaurantTableOrderView>> getRestaurantTableOrders(
    //     @RequestHeader("Authorization") String authHeader
    // ) {
    //     String token = authHeader.replace("Bearer ", "");
    //     Long restaurantId = jwtService.getRestaurantId(token);

    //     List<RestaurantTableOrderView> orders = employeeService.getRestaurantTableOrdersFromView(restaurantId);
    //     return ResponseEntity.ok(orders);
    // }
}
