package com.tfg_rm.backend_restaurantmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemRequest {
    private Long dishId;
    private String itemNotes;
}