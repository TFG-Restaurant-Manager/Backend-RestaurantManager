package com.tfg_rm.backend_restaurantmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private Long orderId;
    private String type;
    private String status;
    private BigDecimal total;
    private String notes;
    private String createdAt;

    private List<OrderItemRequest> items;

    private LocalDateTime pickupTime;

    private String deliveryAddress;
    private Long clientId;

    private Long tableId;    
}
