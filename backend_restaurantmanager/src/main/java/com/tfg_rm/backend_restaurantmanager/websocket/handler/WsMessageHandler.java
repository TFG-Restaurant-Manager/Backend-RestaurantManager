package com.tfg_rm.backend_restaurantmanager.websocket.handler;

import org.springframework.web.socket.WebSocketSession;

import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsMessageType;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsOutboundMessage;

import tools.jackson.databind.JsonNode;

public interface WsMessageHandler {
    WsMessageType handles();
    WsOutboundMessage<?> handle(WebSocketSession session, JsonNode payload) throws Exception;
}