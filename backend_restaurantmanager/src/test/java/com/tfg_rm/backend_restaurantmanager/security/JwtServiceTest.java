package com.tfg_rm.backend_restaurantmanager.security;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("test-secret-key-must-be-at-least-32-chars!!");
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void generateToken_containsCorrectRestaurantId() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        assertThat(jwtService.getRestaurantId(token)).isEqualTo(10L);
    }

    @Test
    void generateToken_containsCorrectUserId() {
        String token = jwtService.generateToken(42L, 10L, Role.MANAGER);
        assertThat(jwtService.getUserId(token)).isEqualTo(42L);
    }

    @Test
    void generateToken_containsCorrectRole() {
        String token = jwtService.generateToken(1L, 10L, Role.COOKER);
        assertThat(jwtService.getRole(token)).isEqualTo("COOKER");
    }

    @Test
    void validateToken_validToken_returnsTrue() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        assertThat(jwtService.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_tamperedToken_returnsFalse() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertThat(jwtService.validateToken(tampered)).isFalse();
    }

    @Test
    void validateToken_randomString_returnsFalse() {
        assertThat(jwtService.validateToken("not.a.token")).isFalse();
    }

    @Test
    void validateToken_tokenSignedWithDifferentSecret_returnsFalse() {
        JwtService other = new JwtService("completely-different-secret-at-least-32chars!!");
        String foreignToken = other.generateToken(1L, 10L, Role.WAITER);
        assertThat(jwtService.validateToken(foreignToken)).isFalse();
    }

    @Test
    void getRestaurantId_returnsCorrectValue() {
        String token = jwtService.generateToken(1L, 99L, Role.ADMIN);
        assertThat(jwtService.getRestaurantId(token)).isEqualTo(99L);
    }

    @Test
    void getUserId_returnsCorrectValue() {
        String token = jwtService.generateToken(77L, 10L, Role.WAITER);
        assertThat(jwtService.getUserId(token)).isEqualTo(77L);
    }
}
