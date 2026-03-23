package com.tfg_rm.backend_restaurantmanager.shared.entity.view;

import java.math.BigDecimal;
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
    @Column(name = "table_id")
    private Long tableId;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "pos_x")
    private Integer posX;

    @Column(name = "pos_y")
    private Integer posY;

    @Column(name = "status")
    private String status;

    @Column(name = "section_title")
    private String sectionTitle;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "order_total")
    private BigDecimal orderTotal;

    @Column(name = "order_notes")
    private String orderNotes;

    @Column(name = "order_created_at")
    private LocalDateTime orderCreatedAt;

    @Column(name = "dish_name")
    private String dishName;

    @Column(name = "dish_price")
    private BigDecimal dishPrice;

    @Column(name = "category_name")
    private String categoryName;
}
