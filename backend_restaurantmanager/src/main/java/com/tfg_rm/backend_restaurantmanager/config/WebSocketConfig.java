package com.tfg_rm.backend_restaurantmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.tfg_rm.backend_restaurantmanager.handler.RestaurantWebSocketHandler;

/**
 * Java class used to configure the web sockets of the backend
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * Method used to register the main handlers of the web sockets with wich the client is going to send the request
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new RestaurantWebSocketHandler(), "/ws")
                .setAllowedOrigins("*");
    }
}