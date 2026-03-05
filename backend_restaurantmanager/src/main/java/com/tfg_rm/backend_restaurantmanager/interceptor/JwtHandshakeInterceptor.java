package com.tfg_rm.backend_restaurantmanager.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.tfg_rm.backend_restaurantmanager.service.JwtService;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return false;
            }

            String token = authHeader.substring(7);

            if (!jwtService.validateToken(token)) {
                return false;
            }

            Long restaurantId = jwtService.getRestaurantId(token);
            Long userId = jwtService.getUserId(token);
            String role = jwtService.getRole(token);

            attributes.put("restaurantId", restaurantId);
            attributes.put("userId", userId);
            attributes.put("role", role);

            return true;
        }

        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}