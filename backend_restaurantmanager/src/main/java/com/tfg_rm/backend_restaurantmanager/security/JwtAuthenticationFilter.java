package com.tfg_rm.backend_restaurantmanager.security;

import com.tfg_rm.backend_restaurantmanager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Obtener el token del header Authorization
            String token = extractToken(request);

            if (token != null && jwtService.validateToken(token)) {
                    // Token es válido, crear autenticación
                    Long userId = jwtService.getUserId(token);
                    String role = jwtService.getRole(token); // e.g. "CLIENTE", "EMPLEADO"
                    // convertir a autoridad Spring Security (ROLE_ prefix)
                    String authority = "ROLE_" + role.toUpperCase();
                    var authorities = new java.util.ArrayList<org.springframework.security.core.GrantedAuthority>();
                    authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(authority));

                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            userId.toString(), 
                            null, 
                            authorities
                        );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
        } catch (Exception e) {
            logger.error("No se pudo establecer autenticación del usuario", e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
