package com.tfg_rm.backend_restaurantmanager.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantTableOrderView {
    private Long tableId;
    private Long restaurantId;
    private String tableName;
    private Integer capacity;
    private Integer posX;
    private Integer posY;
    private String status;
    private String sectionTitle;
    private Long orderId;
    private String orderStatus;
    private BigDecimal orderTotal;
    private String orderNotes;
    private LocalDateTime orderCreatedAt;
    private String dishName;
    private BigDecimal dishPrice;
    private String categoryNam;
}
