package com.tfg_rm.backend_restaurantmanager.shared.entity.view;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "restaurant_table_orders")
public class RestaurantTableOrderViewEntity {
    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "status_name")
    private String statusName;

    private java.math.BigDecimal total;

    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "dish_id")
    private Integer dishId;
}
