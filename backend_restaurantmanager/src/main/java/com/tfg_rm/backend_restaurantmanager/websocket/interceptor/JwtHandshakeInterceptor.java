package com.tfg_rm.backend_restaurantmanager.websocket.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

import java.util.Map;

/**
 * Interceptor for WebSocket handshake that validates the JWT token and extracts user information.
 */
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    /** The JWT service for token validation and extraction. */
    private final JwtService jwtService;

    /**
     * Constructor for JwtHandshakeInterceptor.
     * @param jwtService The JWT service for token validation and extraction.
     */
    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    
    /**
     * Checks if the JWT token is valid and extracts user information (restaurant ID, user ID, role) from the token.
     * If the token is invalid, returns false.
     * If the token is valid, returns true and adds the user information to the attributes map.
     * @param request the request to be checked
     * @param response the response to return if the token is invalid
     * @param wsHandler the web socket handler
     * @param attributes the attributes map to add the user information
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes
    ) {
        boolean valid = false;

        // Check if the request is an instance of ServletServerHttpRequest to access the HTTP headers
        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String authHeader = httpRequest.getHeader("Authorization");

            // Check if the Authorization header is present and starts with "Bearer "
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                
                // Extract the token from the Authorization header
                String token = authHeader.substring(7);

                // Validate the token and extract user information if valid
                if (jwtService.validateToken(token)) {
                    Long restaurantId = jwtService.getRestaurantId(token);
                    Long userId = jwtService.getUserId(token);
                    String role = jwtService.getRole(token);

                    attributes.put("restaurantId", restaurantId);
                    attributes.put("userId", userId);
                    attributes.put("role", role);

                    valid = true;
                }
            }
        }
        return valid;
    }

    /**
     * Called after the handshake between the client and server is complete.
     * @param request the server request
     * @param response the server response
     * @param wsHandler the web socket handler
     * @param exception the exception thrown during the handshake if any
     */
    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}