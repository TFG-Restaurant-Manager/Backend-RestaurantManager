package com.tfg_rm.backend_restaurantmanager.websocket.dispatcher;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.tfg_rm.backend_restaurantmanager.websocket.dto.ErrorPayload;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsInboundMessage;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsMessageType;
import com.tfg_rm.backend_restaurantmanager.websocket.dto.WsOutboundMessage;
import com.tfg_rm.backend_restaurantmanager.websocket.handler.WsMessageHandler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class WsMessageDispatcher {

    private final List<WsMessageHandler> handlerList;
    private final ObjectMapper objectMapper;

    private Map<WsMessageType, WsMessageHandler> handlers;

    @PostConstruct
    public void init() {
        handlers = handlerList.stream()
            .collect(Collectors.toMap(WsMessageHandler::handles, h -> h));
    }

    /**
     * Parsea el mensaje entrante, delega al handler correcto
     * y devuelve el JSON del mensaje de respuesta.
     */
    public WsOutboundMessage<?> dispatch(WebSocketSession session, String rawMessage) throws Exception {
        WsInboundMessage inbound = objectMapper.readValue(rawMessage, WsInboundMessage.class);

        WsMessageType type;
        try {
            type = WsMessageType.valueOf(inbound.getType());
        } catch (IllegalArgumentException e) {
            WsOutboundMessage<?> error = new WsOutboundMessage<>(
                "FAILED_UNKNOWN_TYPE",
                new ErrorPayload("UNKNOWN_TYPE", "Type not supported: " + inbound.getType())
            );
            return error;
        }

        WsMessageHandler handler = handlers.get(type);
        if (handler == null) {
            WsOutboundMessage<?> error = new WsOutboundMessage<>(
                "FAILED_UNHANDLED_TYPE",
                new ErrorPayload("UNHANDLED_TYPE", "No handler registered for: " + type)
            );
            return error;
        }

        WsOutboundMessage<?> outbound = handler.handle(session, inbound.getPayload());
        return outbound;
    }
}