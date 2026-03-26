package com.tfg_rm.backend_restaurantmanager.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishesResponse {
    private Long id;
    private String name;
    private String description;
    private String categoryName;
    private BigDecimal price;
    private Boolean available;
}
