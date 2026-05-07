package com.tfg_rm.backend_restaurantmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemRequest {
    private Long dishId;
    private Long id;
    private String itemNotes;
    private String status;
}