package com.tfg_rm.backend_restaurantmanager.websocket.dto;

/**
 * Enum used to represent the type of message
 */
public enum WsMessageType {

    /** The inbound type of message */
    CREATE_ORDER,
    UPDATE_ORDER,

    /** The outbound type of message */
    ORDER_CREATED,
    ORDER_UPDATED,

    /** The outbound type of message */
    FAILED_CREATE_ORDER,
    FAILED_UPDATE_ORDER;
}
