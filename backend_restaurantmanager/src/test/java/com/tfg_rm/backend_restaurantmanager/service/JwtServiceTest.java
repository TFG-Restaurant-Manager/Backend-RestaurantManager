package com.tfg_rm.backend_restaurantmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.tfg_rm.backend_restaurantmanager.auth.dto.Role;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

public class JwtServiceTest {
    private final JwtService jwtService =
            new JwtService("clave_super_larga_y_segura_para_firmar_tokens_123456");

    @Test
    void shouldGenerateValidToken() {

        String token = jwtService.generateToken(1, 5, Role.WAITER);

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void shouldExtractRestaurantId() {

        String token = jwtService.generateToken(1, 5, Role.ADMIN);

        Long restaurantId = jwtService.getRestaurantId(token);

        assertEquals(5L, restaurantId);
    }

    @Test
    void shouldExtractRole() {

        String token = jwtService.generateToken(1, 5, Role.WAITER);

        String role = jwtService.getRole(token);

        assertEquals("WAITER", role);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {

        boolean valid = jwtService.validateToken("token_invalido");

        assertFalse(valid);
    }
}
