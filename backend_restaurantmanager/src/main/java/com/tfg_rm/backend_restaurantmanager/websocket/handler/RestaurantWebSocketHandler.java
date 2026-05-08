package com.tfg_rm.backend_restaurantmanager.websocket.handler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.tfg_rm.backend_restaurantmanager.websocket.dispatcher.WsMessageDispatcher;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsOutboundMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/**
 * WebSocket handler for restaurant management.
 * It manages WebSocket sessions for different restaurants
 * and allows sending messages to clients connected to the same restaurant.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantWebSocketHandler extends TextWebSocketHandler {

    /**
     * List of restaurants that are conected with the websockets
     */
    private static final Map<Long, Set<WebSocketSession>> restaurantSessions = new ConcurrentHashMap<>();

    private final WsMessageDispatcher wsMessageDispatcher;
    private final ObjectMapper objectMapper;

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
        } else {

            restaurantSessions
                    .computeIfAbsent(restaurantId, k -> ConcurrentHashMap.newKeySet())
                    .add(session);

            log.info("Restaurant connected: " + restaurantId);
        }
    }

    /**
     * Method for when the client send a message to the websocket
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        // Gets the restaurant id from the session
        Long restaurantId = (Long) session.getAttributes().get("restaurantId");

        if (restaurantId != null) {

            WsOutboundMessage<?> outbound = wsMessageDispatcher.dispatch(session, message.getPayload());
            String responseJson = objectMapper.writeValueAsString(outbound);

            if (outbound.getType().startsWith("FAILED_")) {
                session.sendMessage(new TextMessage(responseJson));
            } else {
                // It send the messages only to those who are from the same restaurant
                for (WebSocketSession s : restaurantSessions.getOrDefault(restaurantId, Set.of()))
                    if (s.isOpen()) s.sendMessage(new TextMessage(responseJson));
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