package com.tfg_rm.backend_restaurantmanager.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableResponse {
    private Long tableId;
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
    private String orderCreatedAt;

    private List<OrderItemResponse> orderItems;
}