package com.tfg_rm.backend_restaurantmanager.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class used to represent the error payload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorPayload {

    /** The error code */
    private String code;

    /** The error message */
    private String message;
}