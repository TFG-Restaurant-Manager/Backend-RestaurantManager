package com.tfg_rm.backend_restaurantmanager.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private String type;
    private String notes;

    private List<OrderItemRequest> items;

    private LocalDateTime pickupTime;

    private String deliveryAddress;
    private Long clientId;

    private Long tableId;    
}
