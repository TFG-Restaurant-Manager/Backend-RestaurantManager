package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.ClientLoginRequest;
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
     * Endpoint for client login. Validates the client's credentials and generates a JWT token if successful.
     * @param request The client's login request containing email, password, and restaurant ID.
     * @return The login response containing the generated JWT token.
     * @throws DataInvalid 
     */
    @PostMapping("/clientLogin")
    public ResponseEntity<LoginResponse> clientLogin(@RequestBody ClientLoginRequest request) {
        
        Long userId = authService.checkCredentials(request.getRestaurantId(), request.getEmail(), request.getPassword());
        if (userId == -1L) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Long restaurantId = request.getRestaurantId();
        Role role = Role.CLIENT;

        String token = jwtService.generateToken(userId, restaurantId, role);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    /**
     * Endpoint for client registration. Registers a new client and generates a JWT token for the newly registered client.
     * @param request The client's registration request containing email, password, and restaurant ID.
     * @return The login response containing the generated JWT token.
     */
    @PostMapping("/clientRegister")
    public ResponseEntity<LoginResponse> clientRegister(@RequestBody ClientLoginRequest request) {

        Long userId = authService.addClient(request.getRestaurantId(), request.getEmail(), request.getPassword());
        // Aqui tenemos que validar las credenciales desde la base de datos, esto es solo un ejemplo
        if (userId == -1L) {
            // Tendriamos que lanzar una excepción personalizada para manejar este error de forma adecuada
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Long restaurantId = request.getRestaurantId();
        Role role = Role.CLIENT;

        String token = jwtService.generateToken(userId, restaurantId, role);

        return ResponseEntity.ok(new LoginResponse(token));
    }

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

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
