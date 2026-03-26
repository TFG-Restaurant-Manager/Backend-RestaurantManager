package com.tfg_rm.backend_restaurantmanager.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemResponse {
    private Long orderItemId;
    private Long dishId;
    private String dishName;
    private String itemNotes;
    private BigDecimal orderItemPrice;
}
