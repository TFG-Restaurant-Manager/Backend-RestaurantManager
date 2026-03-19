package com.tfg_rm.backend_restaurantmanager.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantTableOrderView {
    private Integer orderId;
    private Integer restaurantId;
    private Integer tableId;
    private String statusName;
    private BigDecimal total;
    private String notes;
    private LocalDateTime createdAt;
    private Integer dishId;
}
