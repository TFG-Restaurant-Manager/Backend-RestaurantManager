package com.tfg_rm.backend_restaurantmanager.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsInboundMessage {
    private String type;
    private JsonNode payload;
}