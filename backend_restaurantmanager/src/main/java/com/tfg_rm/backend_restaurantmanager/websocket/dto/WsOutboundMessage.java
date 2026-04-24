package com.tfg_rm.backend_restaurantmanager.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class used to represent the outbound message
 * 
 * @param <T> The type of the payload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsOutboundMessage<T> {

    /** The type of the message */
    private String type;

    /** The payload of the message */
    private T payload;
}