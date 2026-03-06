package com.tfg_rm.backend_restaurantmanager.auth.controller;

import com.tfg_rm.backend_restaurantmanager.auth.dto.ClientLoginRequest;
import com.tfg_rm.backend_restaurantmanager.auth.dto.EmployeeLoginRequest;
import com.tfg_rm.backend_restaurantmanager.auth.dto.EmployeeLoginResponse;
import com.tfg_rm.backend_restaurantmanager.auth.dto.EmployeeTokenRequest;
import com.tfg_rm.backend_restaurantmanager.auth.dto.LoginResponse;
import com.tfg_rm.backend_restaurantmanager.auth.dto.Role;
import com.tfg_rm.backend_restaurantmanager.auth.service.AuthService;
import com.tfg_rm.backend_restaurantmanager.shared.exception.InvalidCredentialsException;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public LoginResponse clientLogin(@RequestBody ClientLoginRequest request) {
        
        Long userId = authService.checkCredentials(request.getRestaurantId(), request.getEmail(), request.getPassword());
        if (userId == -1L) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Si las credenciales son correctas, generamos un token JWT con la información del usuario
        Long restaurantId = (long) request.getRestaurantId();
        Role role = Role.CLIENT;

        String token = jwtService.generateToken(userId, restaurantId, role);

        return new LoginResponse(token);
    }

    @PostMapping("/clientRegister")
    public LoginResponse clientRegister(@RequestBody ClientLoginRequest request) {
        
        Long userId = authService.checkCredentials(request.getRestaurantId(), request.getEmail(), request.getPassword());
        // Aqui tenemos que validar las credenciales desde la base de datos, esto es solo un ejemplo
        if (userId == -1L) {
            // Tendriamos que lanzar una excepción personalizada para manejar este error de forma adecuada
            throw new RuntimeException("Credenciales incorrectas");
        }

        // Si las credenciales son correctas, generamos un token JWT con la información del usuario
        Long restaurantId = (long) request.getRestaurantId();
        Role role = Role.CLIENT;

        String token = jwtService.generateToken(userId, restaurantId, role);

        return new LoginResponse(token);
    }

    /**
     * Endpoint for employee login. Validates the employee's credentials and returns their ID and accessible restaurant IDs if successful.
     * @param request The employee's login request containing DNI and password.
     * @return The login response containing the employee's ID and accessible restaurant IDs.
     */
    @PostMapping("/employeeLogin")
    public EmployeeLoginResponse employeeLogin(@RequestBody EmployeeLoginRequest request) {

        // Aqui tenemos que validar las credenciales desde la base de datos, esto es solo un ejemplo
        if (!request.getDni().equals("12345678") ||
                !request.getPassword().equals("1234")) {
            // Tendriamos que lanzar una excepción personalizada para manejar este error de forma adecuada
            throw new RuntimeException("Credenciales incorrectas");
        }

        Long employeeId = 1L;
        // Lista de restaurantes a los que el empleado tiene acceso
        List<Long> restaurantIds = List.of(5L, 10L, 15L);

        return new EmployeeLoginResponse(employeeId, restaurantIds);
    }

    /**
     * Endpoint for generating a JWT token for an employee. Validates the employee's access to the specified restaurant and generates a token if successful.
     * @param request The request containing the employee's ID and the restaurant's ID.
     * @return The response containing the generated JWT token.
     */
    @PostMapping("/employeeToken")
    public LoginResponse employeeToken(@RequestBody EmployeeTokenRequest request) {

        // Validar que el employeeId y restaurantId no sean nulos
        if (request.getEmployeeId() == null || request.getRestaurantId() == null) {
            throw new RuntimeException("Faltan datos: employeeId y restaurantId");
        }

        // Aquí deberías validar que el empleado tenga acceso a ese restaurante
        // en la base de datos

        Long userId = request.getEmployeeId();
        Long restaurantId = request.getRestaurantId();
        Role role = Role.KITCHEN;

        // A JWT token is generated with the employee's information and the restaurant they are accessing
        String token = jwtService.generateToken(userId, restaurantId, role);

        return new LoginResponse(token);
    }
}
