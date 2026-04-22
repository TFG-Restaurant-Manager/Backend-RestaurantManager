package com.tfg_rm.backend_restaurantmanager.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorPayload {
    private String code;
    private String message;
}