package com.tfg_rm.backend_restaurantmanager.handler;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class RestaurantWebSocketHandler extends TextWebSocketHandler {

    /**
     * List of restaurants that are conected with the websockets
     */
    private static final Map<Long, Set<WebSocketSession>> restaurantSessions =
            new ConcurrentHashMap<>();

    /**
     * Method used after the client makes conection with the websocket.
     * 
     * It checks the restaurant sesion in the request and gets the client in the conversation of the introduced restaurant id if its not null
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // Gets the restaurant id
        Long restaurantId = getRestaurantId(session);

        // Checks if it's not null
        if (restaurantId == null) {
            session.close();
            return;
        }

        // Checks if is in the initialized restaurant sessions and if it's not it put it in the sessions 
        restaurantSessions
                .computeIfAbsent(restaurantId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        // It adds to the session the restaurant id for the responses of the websocket
        session.getAttributes().put("restaurantId", restaurantId);

        // Basic logger to notice the conection
        System.out.println("Conectado al restaurante " + restaurantId);
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
        if (restaurantId == null) return;

        // It send the messages only to those who are from the same restaurant
        for (WebSocketSession s : restaurantSessions.getOrDefault(restaurantId, Set.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage("Restaurante " + restaurantId +
                        ": " + message.getPayload()));
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

    /**
     * Method to get the session id from the session
     * @param session
     * @return
     */
    private Long getRestaurantId(WebSocketSession session) {
        // Gets the entire uri of the request
        URI uri = session.getUri();
        if (uri == null) return null;

        // Gets the query of the request that has the restaurant id
        String query = uri.getQuery();
        if (query == null) return null;

        // Returns the restaurant id if nothing is wrong
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("restaurantId")) {
                return Long.parseLong(pair[1]);
            }
        }

        // Returns null if something is wrong
        return null;
    }
}
