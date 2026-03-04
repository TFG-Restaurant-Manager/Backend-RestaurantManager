package com.tfg_rm.backend_restaurantmanager.handler;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class RestaurantWebSocketHandler extends TextWebSocketHandler {

    // restaurantId -> lista de sesiones
    private static final Map<Long, Set<WebSocketSession>> restaurantSessions =
            new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Long restaurantId = getRestaurantId(session);

        if (restaurantId == null) {
            session.close();
            return;
        }

        restaurantSessions
                .computeIfAbsent(restaurantId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        session.getAttributes().put("restaurantId", restaurantId);

        System.out.println("Conectado al restaurante " + restaurantId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        Long restaurantId = (Long) session.getAttributes().get("restaurantId");

        if (restaurantId == null) return;

        // Enviar solo a los de ese restaurante
        for (WebSocketSession s : restaurantSessions.getOrDefault(restaurantId, Set.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage("Restaurante " + restaurantId +
                        ": " + message.getPayload()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        Long restaurantId = (Long) session.getAttributes().get("restaurantId");

        if (restaurantId != null) {
            restaurantSessions.getOrDefault(restaurantId, Set.of()).remove(session);
        }
    }

    private Long getRestaurantId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return null;

        String query = uri.getQuery(); // restaurantId=5
        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("restaurantId")) {
                return Long.parseLong(pair[1]);
            }
        }

        return null;
    }
}
