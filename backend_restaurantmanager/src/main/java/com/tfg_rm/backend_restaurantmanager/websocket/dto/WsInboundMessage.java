package com.tfg_rm.backend_restaurantmanager.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;

/**
 * Class used to represent the inbound message
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsInboundMessage {

    /** The type of the message */
    private String type;

    /** The payload of the message */
    private JsonNode payload;
}