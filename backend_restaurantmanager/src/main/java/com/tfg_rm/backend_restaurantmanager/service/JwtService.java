package com.tfg_rm.backend_restaurantmanager.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    // Clave secreta
    private final String SECRET = "clave_super_larga_y_segura_para_firmar_tokens_123456";

    // Key para firmar/verificar
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(Long userId, Long restaurantId, String role) {

        return Jwts.builder()
                .subject(userId.toString())
                .claim("restaurantId", restaurantId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    // ===============================
    // VALIDAR TOKEN
    // ===============================
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // ===============================
    // OBTENER CLAIMS
    // ===============================
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getRestaurantId(String token) {
        Claims claims = getClaims(token);
        return claims.get("restaurantId", Long.class);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        // El userId está guardado como subject
        return Long.parseLong(claims.getSubject());
    }

    public String getRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }
}