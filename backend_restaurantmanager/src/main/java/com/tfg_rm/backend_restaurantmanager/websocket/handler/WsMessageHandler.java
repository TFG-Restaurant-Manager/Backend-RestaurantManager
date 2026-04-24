package com.tfg_rm.backend_restaurantmanager.websocket.handler;

import org.springframework.web.socket.WebSocketSession;

import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsMessageType;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsOutboundMessage;

import tools.jackson.databind.JsonNode;

/**
 * Interface used to define the methods that a web socket handler must implement
 */
public interface WsMessageHandler {

    /**
     * Method used to specify the type of message this handler handles
     * @return The type of message this handler handles
     */
    WsMessageType handles();

    /**
     * Method used to handle the message
     * @param session The WebSocket session
     * @param payload The JSON payload
     * @return The outbound message
     */
    WsOutboundMessage<?> handle(WebSocketSession session, JsonNode payload) throws Exception;
}