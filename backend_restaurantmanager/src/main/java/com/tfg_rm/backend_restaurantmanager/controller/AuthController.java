package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.login.LoginRequest;
import com.tfg_rm.backend_restaurantmanager.dto.login.LoginResponse;
import com.tfg_rm.backend_restaurantmanager.service.JwtService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        // ⚠️ Aquí deberías consultar tu base de datos
        // esto es solo ejemplo

        if (!request.getEmail().equals("test@test.com") ||
                !request.getPassword().equals("1234")) {

            throw new RuntimeException("Credenciales incorrectas");
        }

        Long userId = 1L;
        Long restaurantId = 5L;
        String role = "CAMARERO";

        String token = jwtService.generateToken(userId, restaurantId, role);

        return new LoginResponse(token);
    }
}
