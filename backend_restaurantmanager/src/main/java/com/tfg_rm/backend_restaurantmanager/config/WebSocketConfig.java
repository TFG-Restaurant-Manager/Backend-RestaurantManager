package com.tfg_rm.backend_restaurantmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.websocket.handler.RestaurantWebSocketHandler;
import com.tfg_rm.backend_restaurantmanager.websocket.interceptor.JwtHandshakeInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * Java class used to configure the web sockets of the backend
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor

public class WebSocketConfig implements WebSocketConfigurer {

    /** The JWT service for authentication. */
    private final JwtService jwtService;
    private final RestaurantWebSocketHandler restaurantWebSocketHandler;

    /**
     * Method used to register the main handlers of the web sockets with wich the client is going to send the request
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(restaurantWebSocketHandler, "/ws")
                .addInterceptors(new JwtHandshakeInterceptor(jwtService))
                .setAllowedOrigins("*");
    }
}