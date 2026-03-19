package com.tfg_rm.backend_restaurantmanager.shared.entity.view;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "data_restaurant_dishes")
public class RestaurantDishViewEntity {
    @Id
    private Integer id;

    private String name;

    @Column(name = "category_name")
    private String categoryName;

    private String description;

    private BigDecimal price;

    private Boolean available;

    @Column(name = "restaurant_id")
    private Integer restaurantId;
}
