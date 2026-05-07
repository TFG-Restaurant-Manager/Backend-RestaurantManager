package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.EmployeeLoginRequest;
import com.tfg_rm.backend_restaurantmanager.dto.LoginResponse;
import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.exception.InvalidCredentialsException;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Java class used to manage the authentication of the users of the application
 */
@RequiredArgsConstructor // Me hace un constructor con todos los campos final
@RestController
@RequestMapping("/auth")
public class AuthController {

    /** The JWT service for token generation and validation. */
    private final JwtService jwtService;

    /** The authentication service for user authentication. */
    private final AuthService authService;

    /**
     * Endpoint for generating a JWT token for an employee. Validates the employee's access to the specified restaurant and generates a token if successful.
     * @param request The request containing the employee's ID and the restaurant's ID.
     * @return The response containing the generated JWT token.
     */
    @PostMapping("/employeeLogin")
    public ResponseEntity<LoginResponse> employeeLogin(@RequestBody EmployeeLoginRequest request) {
        String code = request.getCode();
        String password = request.getPassword();

        Long employeeId = authService.validateEmployeeAccess(code, password);
        if (employeeId == -1) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Role role = authService.getEmployeeRole(employeeId);
        Long restaurantId = authService.getEmployeeRestaurantId(employeeId);

        String token = jwtService.generateToken(employeeId, restaurantId, role);

        return ResponseEntity.ok(new LoginResponse(token, role));
    }
}
