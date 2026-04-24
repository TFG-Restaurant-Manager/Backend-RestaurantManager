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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * Class used to dispatch the messages to the correct handler
 */
@Component
@RequiredArgsConstructor
public class WsMessageDispatcher {

    /** The list of handlers */
    private final List<WsMessageHandler> handlerList;

    /** The object mapper for converting JSON to Java objects */
    private final ObjectMapper objectMapper;

    /** The map of handlers */
    private Map<WsMessageType, WsMessageHandler> handlers;

    /**
     * Method used to initialize the dispatcher
     */
    @PostConstruct
    public void init() {
        handlers = handlerList.stream()
            .collect(Collectors.toMap(WsMessageHandler::handles, h -> h));
    }

    /**
     * Method used to dispatch the message to the correct handler
     * 
     * @param session The WebSocket session
     * @param rawMessage The raw message
     * @return The outbound message
     */
    public WsOutboundMessage<?> dispatch(WebSocketSession session, String rawMessage) throws Exception {
        WsInboundMessage inbound = null;

        WsOutboundMessage<?> outbound = null;
        WsMessageType type;
        try {
            inbound = objectMapper.readValue(rawMessage, WsInboundMessage.class);
            type = WsMessageType.valueOf(inbound.getType());

            WsMessageHandler handler = handlers.get(type);
            if (handler == null) {
                outbound = new WsOutboundMessage<>(
                    "FAILED_UNHANDLED_TYPE",
                    new ErrorPayload("UNHANDLED_TYPE", "No handler registered for: " + type)
                );
            } else {
                outbound = handler.handle(session, inbound.getPayload());
            }
        } catch (JacksonException e) {
            outbound = new WsOutboundMessage<>(
                "FAILED_UNHANDLED_MESSAGE",
                new ErrorPayload("UNHANDLED_MESSAGE", "Message not supported: " + rawMessage)
            );
        } catch (IllegalArgumentException e) {
            String typeValue = (inbound != null) ? inbound.getType() : "null";

            outbound = new WsOutboundMessage<>(
                "FAILED_UNKNOWN_TYPE",
                new ErrorPayload("UNKNOWN_TYPE", "Type not supported: " + typeValue)
            );
        } catch (Exception e) {
            outbound = new WsOutboundMessage<>(
                "FAILED_UNHANDLED_MESSAGE",
                new ErrorPayload("UNHANDLED_MESSAGE", "Message not supported: " + rawMessage)
            );
        } 

        return outbound;
    }
}