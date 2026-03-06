package com.tfg_rm.backend_restaurantmanager.handler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket handler for restaurant management. 
 * It manages WebSocket sessions for different restaurants 
 * and allows sending messages to clients connected to the same restaurant.
 */
public class RestaurantWebSocketHandler extends TextWebSocketHandler {

    /**
     * List of restaurants that are conected with the websockets
     */
    private static final Map<Long, Set<WebSocketSession>> restaurantSessions = new ConcurrentHashMap<>();

    /**
     * Method used after the client makes conection with the websocket.
     * 
     * It checks the restaurant sesion in the request and gets the client in the
     * conversation of the introduced restaurant id if its not null
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        Long restaurantId = (Long) session.getAttributes().get("restaurantId");

        if (restaurantId == null) {
            try {
                session.close();
            } catch (Exception ignored) {
            }
            return;
        }

        restaurantSessions
                .computeIfAbsent(restaurantId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        System.out.println("Conectado restaurante: " + restaurantId);
    }

    /**
     * Method for when the client send a message to the websocket
     * 
     * It checks the restaurant id and if it's not null returns a default message
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        // Gets the restaurant id from the session
        Long restaurantId = (Long) session.getAttributes().get("restaurantId");

        // If there isn't any restaurant id doesn't send any message
        if (restaurantId == null)
            return;

        // It send the messages only to those who are from the same restaurant
        for (WebSocketSession s : restaurantSessions.getOrDefault(restaurantId, Set.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage("Restaurante " + restaurantId +
                        ": " + message.getPayload() + "\nRol client: " + session.getAttributes().get("role")));
            }
        }
    }

    /**
     * Method for when the connection is closed,
     * it removes the restaurant id from the sessions ids
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        // Gets the restaurant id from the session
        Long restaurantId = (Long) session.getAttributes().get("restaurantId");

        // Removes the restaurant id from the sessions
        if (restaurantId != null) {
            restaurantSessions.getOrDefault(restaurantId, Set.of()).remove(session);
        }
    }
}
