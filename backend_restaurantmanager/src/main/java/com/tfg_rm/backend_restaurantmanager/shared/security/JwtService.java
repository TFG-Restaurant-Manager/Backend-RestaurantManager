package com.tfg_rm.backend_restaurantmanager.shared.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.auth.dto.Role;

import java.util.Date;

import javax.crypto.SecretKey;

/**
 * Service for generating and validating JWTs.
 */
@Slf4j
@Service
public class JwtService {

    /** The secret key for signing JWTs. */
    /*
     * @Value("${jwt.secret}")
     * private String SECRET;
     */

    /** Initializes the secret key after Spring injection. */
    /*
     * @PostConstruct
     * private void init() {
     * this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
     * }
     */

    /** The secret key for signing JWTs. */
    private SecretKey key;

    /** Initializes the secret key. */
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a JWT for the given user ID, restaurant ID, and role.
     * 
     * @param userId       the ID of the user
     * @param restaurantId the ID of the restaurant
     * @param role         the role of the user
     * @return the generated JWT as a String
     */
    public String generateToken(Integer userId, Integer restaurantId, Role role) {

        return Jwts.builder()
                .subject(userId.toString())
                .claim("restaurantId", restaurantId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    /**
     * Validates the given JWT.
     * 
     * @param token the JWT to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        boolean isValid = false;
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            isValid = true;
        } catch (JwtException e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Extracts claims from the given JWT.
     * 
     * @param token the JWT from which to extract claims
     * @return the extracted claims
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the restaurant ID from the given JWT.
     * 
     * @param token the JWT from which to extract the restaurant ID
     * @return the extracted restaurant ID
     */
    public Integer getRestaurantId(String token) {
        Claims claims = getClaims(token);
        return claims.get("restaurantId", Integer.class);
    }

    /**
     * Extracts the user ID from the given JWT.
     * 
     * @param token the JWT from which to extract the user ID
     * @return the extracted user ID
     */
    public Integer getUserId(String token) {
        Claims claims = getClaims(token);
        // El userId está guardado como subject
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * Extracts the role from the given JWT.
     * 
     * @param token the JWT from which to extract the role
     * @return the extracted role
     */
    public String getRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }
}