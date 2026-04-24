package com.tfg_rm.backend_restaurantmanager.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.tfg_rm.backend_restaurantmanager.dto.OrderRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.service.OrderService;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.ErrorPayload;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsMessageType;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsOutboundMessage;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Handler for creating orders via WebSocket.
 */
@Component
@RequiredArgsConstructor
public class CreateOrderHandler implements WsMessageHandler {

    /** The order service for creating orders */
    private final OrderService orderService;

    /** The object mapper for converting JSON to Java objects */
    private final ObjectMapper objectMapper;

    /**
     * Method used to specify the type of message this handler handles
     * 
     * @return The type of message this handler handles
     */
    @Override
    public WsMessageType handles() {
        return WsMessageType.CREATE_ORDER;
    }

    /**
     * Method used to handle the message
     * 
     * @param session The WebSocket session
     * @param payload The JSON payload
     * @return The outbound message
     */
    @Override
    public WsOutboundMessage<?> handle(WebSocketSession session, JsonNode payload) {
        WsOutboundMessage<?> message = null;
        try {
            OrderRequest request = objectMapper.treeToValue(payload, OrderRequest.class);
            Long restaurantId = (Long) session.getAttributes().get("restaurantId");

            OrderResponse created = orderService.createOrder(restaurantId, request);

            message = new WsOutboundMessage<>(
                WsMessageType.ORDER_CREATED.name(),
                created
            );
        } catch (Exception e) {
            message = new WsOutboundMessage<>(
                WsMessageType.FAILED_CREATE_ORDER.name(),
                new ErrorPayload("CREATE_ERROR", e.getMessage())
            );
        }
        return message;
    }
}