package com.tfg_rm.backend_restaurantmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.websocket.handler.RestaurantWebSocketHandler;
import com.tfg_rm.backend_restaurantmanager.websocket.interceptor.JwtHandshakeInterceptor;

/**
 * Java class used to configure the web sockets of the backend
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /** The JWT service for authentication. */
    private final JwtService jwtService;

    /**
     * Constructor for WebSocketConfig.
     * @param jwtService The JWT service for token validation and extraction.
     */
    public WebSocketConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Method used to register the main handlers of the web sockets with wich the client is going to send the request
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new RestaurantWebSocketHandler(), "/ws")
                .addInterceptors(new JwtHandshakeInterceptor(jwtService))
                .setAllowedOrigins("*");
    }
}