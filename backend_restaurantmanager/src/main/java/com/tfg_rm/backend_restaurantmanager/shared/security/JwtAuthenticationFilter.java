package com.tfg_rm.backend_restaurantmanager.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter of JWT authentication that runs once per request.
 * It checks the JWT token in the Authorization header and sets the authentication in the security context if the token is valid.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** The JWT service for token validation and extraction. */
    private final JwtService jwtService;

    /**
     * Filters the HTTP request to authenticate the user based on the JWT token.
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract the token from the Authorization header
            String token = extractToken(request);

            if (token != null && jwtService.validateToken(token)) {
                    // If the token is valid, extract user details and set authentication
                    Integer userId = jwtService.getUserId(token);
                    Integer restaurantId = jwtService.getRestaurantId(token);
                    String role = jwtService.getRole(token);

                    CustomUserDetails userDetails = new CustomUserDetails(
                        userId,
                        restaurantId,
                        role
                    );

                    /* Create the authentication token */
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );

                    /* Set the authentication in the security context */
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
        } catch (Exception e) {
            log.error("Could not set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     * @param request the HTTP request
     * @return the extracted JWT token, or null if not found
     */
    private String extractToken(HttpServletRequest request) {
        String jwtToken = null;
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
        }
        return jwtToken;
    }
}
