package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.RestaurantDto;
import com.tfg_rm.backend_restaurantmanager.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CamareroController {

    private final JwtService jwtService;

    public CamareroController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/mi-restaurante")
    public RestaurantDto obtenerRestaurante(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtService.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }
        Long restaurantId = jwtService.getRestaurantId(token);
        // aquí podrías llamar a un servicio para obtener más datos
        return new RestaurantDto(restaurantId, "Restaurante " + restaurantId);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}