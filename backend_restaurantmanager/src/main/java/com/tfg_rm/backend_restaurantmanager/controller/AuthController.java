package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.login.ClientLoginRequest;
import com.tfg_rm.backend_restaurantmanager.dto.login.EmployeeLoginRequest;
import com.tfg_rm.backend_restaurantmanager.dto.login.EmployeeLoginResponse;
import com.tfg_rm.backend_restaurantmanager.dto.login.EmployeeTokenRequest;
import com.tfg_rm.backend_restaurantmanager.dto.login.LoginResponse;
import com.tfg_rm.backend_restaurantmanager.dto.login.Role;
import com.tfg_rm.backend_restaurantmanager.service.JwtService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/clientLogin")
    public LoginResponse clientLogin(@RequestBody ClientLoginRequest request) {

        // ⚠️ Aquí deberías consultar tu base de datos
        // esto es solo ejemplo

        if (!request.getEmail().equals("test@test.com") ||
                !request.getPassword().equals("1234") ||
                request.getRestaurantId() != 5) {

            throw new RuntimeException("Credenciales incorrectas");
        }

        Long userId = 1L;
        Long restaurantId = 5L;
        Role role = Role.CLIENT;

        String token = jwtService.generateToken(userId, restaurantId, role);

        return new LoginResponse(token);
    }

    @PostMapping("/employeeLogin")
    public EmployeeLoginResponse employeeLogin(@RequestBody EmployeeLoginRequest request) {

        // ⚠️ Aquí deberías consultar tu base de datos
        // esto es solo ejemplo

        if (!request.getDni().equals("12345678") ||
                !request.getPassword().equals("1234")) {

            throw new RuntimeException("Credenciales incorrectas");
        }

        Long employeeId = 1L;
        // Lista de restaurantes a los que el empleado tiene acceso
        List<Long> restaurantIds = List.of(5L, 10L, 15L);

        return new EmployeeLoginResponse(employeeId, restaurantIds);
    }

    @PostMapping("/employeeToken")
    public LoginResponse employeeToken(@RequestBody EmployeeTokenRequest request) {

        // Validar que el employeeId y restaurantId no sean nulos
        if (request.getEmployeeId() == null || request.getRestaurantId() == null) {
            throw new RuntimeException("Faltan datos: employeeId y restaurantId");
        }

        // ⚠️ Aquí deberías validar que el empleado tenga acceso a ese restaurante
        // en la base de datos

        Long userId = request.getEmployeeId();
        Long restaurantId = request.getRestaurantId();
        Role role = Role.KITCHEN;

        String token = jwtService.generateToken(userId, restaurantId, role);

        return new LoginResponse(token);
    }
}
