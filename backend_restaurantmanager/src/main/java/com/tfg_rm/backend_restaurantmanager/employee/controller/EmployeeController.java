package com.tfg_rm.backend_restaurantmanager.employee.controller;

import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.employee.service.EmployeeService;
import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/employeeRegister")
    public ResponseEntity<EmployeeEntity> employeeRegister(@RequestBody EmployeeRegisterRequest request) {;
        EmployeeEntity employee = employeeService.registerEmployee(request);
        return ResponseEntity.ok(employee);
    }
}
