package com.tfg_rm.backend_restaurantmanager.employee.controller;

import com.tfg_rm.backend_restaurantmanager.employee.dto.RestaurantDto;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Este es una clase de ejemplo para mostrar cómo se puede recuperar la información del restaurante al que tiene acceso un camarero a través del token JWT.
 */
@RequiredArgsConstructor
@RestController
public class EmployeeController {

    private final JwtService jwtService;

    @GetMapping("/mi-restaurante")
    public RestaurantDto obtenerRestaurante(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtService.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }
        /* Obtener el ID del restaurante del token */
        Long restaurantId = jwtService.getRestaurantId(token);
        // aquí podrías llamar a un servicio para obtener más datos
        return new RestaurantDto(restaurantId, "Restaurante " + restaurantId);
    }

    /** Método para extraer el token del encabezado de la solicitud */
    private String extractToken(HttpServletRequest request) {
        String token = null;
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        return token;
    }
}
