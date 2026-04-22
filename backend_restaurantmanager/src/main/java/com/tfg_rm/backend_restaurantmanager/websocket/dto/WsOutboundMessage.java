package com.tfg_rm.backend_restaurantmanager.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsOutboundMessage<T> {
    private String type;
    private T payload;
}