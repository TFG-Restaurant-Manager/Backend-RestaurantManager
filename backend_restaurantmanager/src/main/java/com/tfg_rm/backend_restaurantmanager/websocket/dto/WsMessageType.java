package com.tfg_rm.backend_restaurantmanager.websocket.dto;

public enum WsMessageType {
    // Inbound
    CREATE_ORDER,

    // Outbound
    ORDER_CREATED,
    FAILED_CREATE_ORDER;
}
