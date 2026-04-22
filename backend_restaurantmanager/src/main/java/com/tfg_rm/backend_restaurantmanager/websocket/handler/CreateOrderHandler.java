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

@Component
@RequiredArgsConstructor
public class CreateOrderHandler implements WsMessageHandler {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Override
    public WsMessageType handles() {
        return WsMessageType.CREATE_ORDER;
    }

    @Override
    public WsOutboundMessage<?> handle(WebSocketSession session, JsonNode payload) {
        try {
            OrderRequest request = objectMapper.treeToValue(payload, OrderRequest.class);
            Long restaurantId = (Long) session.getAttributes().get("restaurantId");

            OrderResponse created = orderService.createOrder(restaurantId, request);

            return new WsOutboundMessage<>(
                WsMessageType.ORDER_CREATED.name(),
                created
            );
        } catch (Exception e) {
            return new WsOutboundMessage<>(
                WsMessageType.FAILED_CREATE_ORDER.name(),
                new ErrorPayload("CREATE_ERROR", e.getMessage())
            );
        }
    }
}