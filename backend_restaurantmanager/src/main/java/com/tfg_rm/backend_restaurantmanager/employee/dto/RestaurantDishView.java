package com.tfg_rm.backend_restaurantmanager.employee.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantDishView {
    private Integer id;
    private String name;
    private String categoryName;
    private String description;
    private BigDecimal price;
    private Boolean available;
    private Integer restaurantId;
}
