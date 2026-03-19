package com.tfg_rm.backend_restaurantmanager.employee.controller;

import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.employee.service.EmployeeService;
import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
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
}
