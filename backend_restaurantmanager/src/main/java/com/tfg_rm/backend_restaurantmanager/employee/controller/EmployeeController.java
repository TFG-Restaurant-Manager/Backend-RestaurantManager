package com.tfg_rm.backend_restaurantmanager.employee.controller;

import java.util.List;

import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeWithSchedulesResponse;
import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.employee.dto.RestaurantDishView;
import com.tfg_rm.backend_restaurantmanager.employee.dto.RestaurantTableOrderView;
import com.tfg_rm.backend_restaurantmanager.employee.service.EmployeeService;
import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Este es una clase de ejemplo para mostrar cómo se puede recuperar la información del restaurante al que tiene acceso un camarero a través del token JWT.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final JwtService jwtService;

    @PostMapping("/employeeRegister")
    public ResponseEntity<EmployeeEntity> employeeRegister(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody EmployeeRegisterRequest request
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Integer restaurantId = jwtService.getRestaurantId(token);

        EmployeeEntity employee = employeeService.registerEmployee(request, restaurantId);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/getEmployeeInfo")
    public ResponseEntity<EmployeeWithSchedulesResponse> getEmployeeInfo(
        @RequestHeader("Authorization") String authHeader
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Integer restaurantId = jwtService.getRestaurantId(token);
        Integer employeeId = jwtService.getUserId(token);

        EmployeeWithSchedulesResponse info = employeeService.getEmployeeInfo(restaurantId, employeeId);
        return ResponseEntity.ok(info);
    }

    @GetMapping("/getRestaurantDishes")
    public ResponseEntity<List<RestaurantDishView>> getRestaurantDishes(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Integer restaurantId = jwtService.getRestaurantId(token);

        List<RestaurantDishView> dishes = employeeService.getRestaurantDishesFromView(restaurantId);
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/getRestaurantTableOrders")
    public ResponseEntity<List<RestaurantTableOrderView>> getRestaurantTableOrders(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Integer restaurantId = jwtService.getRestaurantId(token);

        List<RestaurantTableOrderView> orders = employeeService.getRestaurantTableOrdersFromView(restaurantId);
        return ResponseEntity.ok(orders);
    }
}
